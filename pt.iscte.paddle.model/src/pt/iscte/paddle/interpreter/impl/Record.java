package pt.iscte.paddle.interpreter.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import pt.iscte.paddle.interpreter.IRecord;
import pt.iscte.paddle.interpreter.IReference;
import pt.iscte.paddle.interpreter.IValue;
import pt.iscte.paddle.model.IRecordType;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariable;

public class Record implements IRecord {
	private final IRecordType type;
	private final Map<IVariable, IReference> fields;
	
	public Record(IRecordType type) {
		this.type = type;
		this.fields = new LinkedHashMap<>();
		for (IVariable var : type.getFields()) {
			IValue val = Value.create(var.getType(), var.getType().getDefaultValue());
			fields.put(var, new Reference(val));
		}
	}
	
	@Override
	public IReference getField(IVariable field) {
		return fields.get(field);
	}
	
	@Override
	public void setField(IVariable field, IValue value) {
		assert fields.containsKey(field) : field;
		fields.put(field, fields.get(field)).setTarget(value);
	}
	
	@Override
	public String toString() {
		String text = "";
		for (Entry<IVariable, IReference> e : fields.entrySet()) {
			text += e.getKey() + " = " + e.getValue() + "\n";
		}
		return text;
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public Map<IVariable, IValue> getValue() {
		return Collections.unmodifiableMap(fields);
	}
	
	@Override
	public IRecord copy() {
		Record record = new Record(type);
		for (IVariable var : type.getFields())
			record.fields.put(var, getField(var).copy());
		return record;
	}
}
