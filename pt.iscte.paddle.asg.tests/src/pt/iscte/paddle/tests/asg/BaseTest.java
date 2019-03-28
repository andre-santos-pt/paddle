package pt.iscte.paddle.tests.asg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import pt.iscte.paddle.asg.IModule;
import pt.iscte.paddle.asg.IProcedure;
import pt.iscte.paddle.asg.IVariable;
import pt.iscte.paddle.asg.semantics.ISemanticProblem;
import pt.iscte.paddle.machine.IExecutionData;
import pt.iscte.paddle.machine.IMachine;
import pt.iscte.paddle.machine.IProgramState;
import pt.iscte.paddle.machine.IValue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class BaseTest {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface Case {
		String[] value();
	}

	final IModule module = IModule.create();

	private IProcedure procedure;
	private IProgramState state;

	@Before
	public void setup() {
		module.setId(getClass().getSimpleName());
		try {
			for (Field f : getClass().getDeclaredFields()) {
				if (f.getType().equals(IProcedure.class)) {
					IProcedure p = (IProcedure) f.get(this);
					p.setId(f.getName());
					if (procedure == null)
						procedure = p;
				} else if (f.getType().equals(IVariable.class))
					((IVariable) f.get(this)).setId(f.getName());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void compile() {
		System.out.println(module);
		List<ISemanticProblem> problems = module.checkSemantics();
		for (ISemanticProblem p : problems) {
			System.err.println(p);
		}
		assertTrue("Semantic errors", problems.isEmpty());
	}

	@Test
	public void run() throws Throwable {
		state = IMachine.create(module);

		for (Method method : getClass().getMethods()) {
			if (method.isAnnotationPresent(Case.class)) {
				if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(IExecutionData.class)) {
					String[] value = method.getAnnotation(Case.class).value();
					Object[] args = new Object[value.length];
					for (int i = 0; i < value.length; i++)
						args[i] = value[i];
					IExecutionData data = runCase(args);
					try {
						method.invoke(this, data);
					} catch (InvocationTargetException e) {
						if (e.getCause() instanceof AssertionError)
							throw e.getCause();
						else
							e.printStackTrace();
					} catch (IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace();
					}
				} else
					System.err.println("Method should have a single parameter of type "
							+ IExecutionData.class.getSimpleName() + " " + method);
			}
		}
	}

	private IExecutionData runCase(Object[] args) {
		IExecutionData data = state.execute(procedure, args);
		assertNotNull(data);
		commonAsserts(data);
		return data;
	}

	protected void commonAsserts(IExecutionData data) {

	}

	protected static void assertEqual(int expected, IValue value) {
		assertTrue("value is not BigDecimal", value.getValue() instanceof BigDecimal);
		assertEquals(expected, ((BigDecimal) value.getValue()).intValue());
	}

	protected static void assertEqual(String expected, IValue value) {
		assertTrue("value is not BigDecimal", value.getValue() instanceof BigDecimal);
		assertEquals(expected, ((BigDecimal) value.getValue()).toString());
	}
}
