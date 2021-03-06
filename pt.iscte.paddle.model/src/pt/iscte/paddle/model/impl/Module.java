package pt.iscte.paddle.model.impl;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Iterables;

import pt.iscte.paddle.model.IConstantDeclaration;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IModel2CodeTranslator;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureDeclaration;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IRecordType;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.commands.IAddCommand;
import pt.iscte.paddle.model.commands.ICommand;
import pt.iscte.paddle.model.commands.IDeleteCommand;
import pt.iscte.paddle.model.validation.AsgSemanticChecks;
import pt.iscte.paddle.model.validation.ISemanticProblem;
import pt.iscte.paddle.model.validation.SemanticChecker;

public class Module extends ListenableProgramElement<IModule.IListener> implements IModule {
	private final List<IConstantDeclaration> constants;
	private final List<IRecordType> records;
	private final List<IProcedure> procedures;

	private final List<IProcedure> builtinProcedures;

	private final History history;
	
	private class History {
		private final ArrayDeque<ICommand<?>> commands = new ArrayDeque<>();
		private final ArrayDeque<ICommand<?>> redo = new ArrayDeque<>();

		void executeCommand(ICommand<?> cmd) {
			cmd.execute();
			commands.push(cmd);
		}

		public void undo() {
			if(!commands.isEmpty()) {
				ICommand<?> cmd = commands.pop();
				System.out.println("UNDO " + cmd.toText());
				cmd.undo();
				redo.push(cmd);
			}
		}

		public void redo() {
			if(!redo.isEmpty())
				executeCommand(redo.pop());
		}
	}

	
	
	public Module(boolean recordHistory) {
		constants = new ArrayList<>();
		records = new ArrayList<>();
		procedures = new ArrayList<>();
		builtinProcedures = new ArrayList<>();
		history = recordHistory ? new History() : null;
	}

	public void loadBuildInProcedures(Class<?> staticClass) {
		for (Method method : staticClass.getDeclaredMethods()) {
			if(BuiltinProcedure.isValidForBuiltin(method))
				builtinProcedures.add(new BuiltinProcedure(this, method));
			else
				System.err.println("not valid for built-in procedure: " + method);
		}
	}
	
	void executeCommand(ICommand<?> cmd) {
		if(history == null)
			cmd.execute();
		else
			history.executeCommand(cmd);
		
//		System.out.println("CMD: " + cmd.toText());
		getListeners().forEachRemaining(l -> l.commandExecuted(cmd));
	}
	
	public void undo() {
		if(history != null)
			history.undo();
	}
	
	public void redo() {
		if(history != null)
			history.redo();
	}

	@Override
	public List<IConstantDeclaration> getConstants() {
		return Collections.unmodifiableList(constants);
	}

	@Override
	public List<IRecordType> getRecordTypes() {
		return Collections.unmodifiableList(records);
	}

	@Override
	public List<IProcedure> getProcedures() {
		return Collections.unmodifiableList(procedures);
	}

	private class AddConstant implements IAddCommand<IConstantDeclaration> {
		final String id;
		final IType type;
		final ILiteral literal;
		IConstantDeclaration constant;
		final String[] flags;
		
		AddConstant(String id, IType type, ILiteral literal, String ... flags) {
			this.id = id;
			this.type = type;
			this.literal = literal;
			this.flags = flags;
		}

		@Override
		public void execute() {
			if(constant == null) {
				constant = new ConstantDeclaration(Module.this, type, literal);
				constant.setId(id);
				for(String f : flags)
					constant.setFlag(f);
			}
			constants.add(constant);
			getListeners().forEachRemaining(l -> l.constantAdded(constant));
		}

		@Override
		public void undo() {
			constants.remove(constant);
			getListeners().forEachRemaining(l -> l.constantRemoved(constant));
		}

		@Override
		public IProgramElement getParent() {
			return Module.this;
		}

		@Override
		public IConstantDeclaration getElement() {
			return constant;
		}
	}

	
	@Override
	public IConstantDeclaration addConstant(String id, IType type, ILiteral value, String ... flags) {
		assert type != null;
		assert value != null;
		AddConstant add = new AddConstant(id, type, value, flags);
		executeCommand(add);
		return add.getElement();
	}

	private class AddRecord implements IAddCommand<IRecordType> {
		final String id;
		private IRecordType type;
		
		AddRecord(String id) {
			this.id = id;
		}
		
		@Override
		public void execute() {
			type = new RecordType(Module.this);
			type.setId(id);
			records.add(type);
		}

		@Override
		public void undo() {
			records.remove(type);
		}

		@Override
		public IRecordType getElement() {
			return type;
		}

		@Override
		public IProgramElement getParent() {
			return Module.this;
		}
	}
	
	@Override
	public IRecordType addRecordType() {
		return addRecordType(null);
	}

	@Override
	public IRecordType addRecordType(String id) {
		AddRecord add = new AddRecord(id);
		executeCommand(add);
		return add.getElement();
	}
	
	private class AddProcedure implements IAddCommand<IProcedure> {
		final String id;
		final IType returnType;
		IProcedure procedure;
		String[] flags;
		int index;
		
		AddProcedure(String id, IType returnType, int index, String ... flags) {
			this.id = id;
			this.returnType = returnType;
			this.index = index;
			this.flags = flags;
		}

		AddProcedure(IProcedure procedure, int index) {
			this.procedure = procedure;
			this.returnType = procedure.getReturnType();
			this.id = procedure.getId();
			this.index = index;
		}
		
		@Override
		public void execute() {
			assert index == -1 || index >= 0 && index <= procedures.size();
			if(procedure == null) {
				procedure = new Procedure(Module.this, returnType);
				procedure.setId(id);
				for(String f : flags)
					procedure.setFlag(f);
			}
			if(index == -1)
				procedures.add(procedure);
			else
				procedures.add(index, procedure);
			
			getListeners().forEachRemaining(l -> l.procedureAdded(procedure));
		}

		@Override
		public void undo() {
			procedures.remove(procedure);
			getListeners().forEachRemaining(l -> l.procedureRemoved(procedure));
		}

		@Override
		public IProgramElement getParent() {
			return Module.this;
		}

		@Override
		public IProcedure getElement() {
			return procedure;
		}
	}


	@Override
	public IProcedure addProcedure(String id, IType returnType, String ... flags) {
		return addProcedureAt(id, returnType, -1, flags);
	}
	
	@Override
	public IProcedure addProcedureAt(String id, IType returnType, int index, String... flags) {
		AddProcedure proc = new AddProcedure(id, returnType, index, flags);
		executeCommand(proc);
		return proc.getElement();
	}
	
	@Override
	public IProcedure getProcedure(String id) {
		for(IProcedure p : procedures)
			if(id.equals(p.getId()))
				return p;
		return null;
	}

	@Override
	public IProcedure resolveProcedure(IProcedureDeclaration procedureDeclaration) {
		for(IProcedure p : Iterables.concat(procedures, builtinProcedures))
			if(p.hasSameSignature(procedureDeclaration))
				return p;

		return null;
	}

	@Override
	public IProcedure resolveProcedure(String id, IType... paramTypes) {
		for(IProcedure p : Iterables.concat(procedures, builtinProcedures))
			if(p.matchesSignature(id, paramTypes))
				return p;

		return null;
	}

	@Override
	public String toString() {
		String text = "";
		for(IConstantDeclaration c : constants)
			text += c.getType() + " " + c.getId() + " = " + c.getValue() + ";\n";

		if(!constants.isEmpty())
			text += "\n";

		for(IRecordType r : records) {
			text += "typedef struct\n{\n";
			for (IVariableDeclaration member : r.getFields()) {
				text += "\t" + member.getDeclaration() + ";\n";
			}
			text += "} " + r.getId() + ";\n\n";
		}

		for (IProcedure p : builtinProcedures)
			System.out.println(p.longSignature() + "\t(built-in)\n");

		text += "\n";

		for (IProcedure p : procedures)
			text += p + "\n\n";

		return text;
	}

	@Override
	public List<ISemanticProblem> checkSemantics() {
		SemanticChecker checker = new SemanticChecker(new AsgSemanticChecks());
		return checker.check(this);
	}

	@Override
	public String translate(IModel2CodeTranslator t) {
		String text = t.header(this);
		for(IConstantDeclaration c : constants)
			text += t.declaration(c);

		for(IRecordType r : records)
			text += t.declaration(r);

		for (IProcedure p : builtinProcedures)
			System.out.println(p.longSignature() + "\t(built-in)\n");

		//		text += "\n";

		for (IProcedure p : procedures)
			text += p.translate(t);

		return text + t.close(this);
	}

	// for tests only
	public void addProcedure(IProcedure p) {
		procedures.add(p);
	}
	
	
	
	@Override
	public void removeProcedure(IProcedure procedure) {
		assert procedures.contains(procedure);
		executeCommand(new RemoveProcedure(procedure));
	}
	
	private class RemoveProcedure implements IDeleteCommand<IProcedure> {
		final IProcedure procedure;
		int index;
		
		public RemoveProcedure(IProcedure procedure) {
			this.procedure = procedure;
		}
		
		@Override
		public void execute() {
			index = procedures.indexOf(procedure);
			procedures.remove(procedure);
			getListeners().forEachRemaining(l -> l.procedureRemoved(procedure));
		}

		@Override
		public void undo() {
			new AddProcedure(procedure, index).execute();
		}

		@Override
		public IProcedure getElement() {
			return procedure;
		}
		
	}
}
