package pt.iscte.paddle.machine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.util.concurrent.ExecutionError;

import pt.iscte.paddle.asg.IArrayType;
import pt.iscte.paddle.asg.IDataType;
import pt.iscte.paddle.asg.IRecordType;
import pt.iscte.paddle.machine.IArray;
import pt.iscte.paddle.machine.IHeapMemory;
import pt.iscte.paddle.machine.IRecord;
import pt.iscte.paddle.machine.IValue;

public class HeapMemory implements IHeapMemory {
	private final ProgramState state;
	private final List<IValue> objects;

	public HeapMemory(ProgramState state) {
		this.state = state;
		objects = new ArrayList<>();
	}

	@Override
	public IArray allocateArray(IDataType baseType, int... dimensions) throws ExecutionError {
		assert dimensions.length > 0 && dimensions[0] >= 0;
		IArrayType arrayType = baseType.array();
		for (int i = 1; i < dimensions.length; i++)
			arrayType = arrayType.array();

		Array array = new Array(arrayType, dimensions[0]);
		if (dimensions.length == 1) {
			for (int i = 0; i < dimensions[0]; i++) {
				IValue val = Value.create(baseType, baseType.getDefaultValue());
				array.setElement(i, val);
			}
		}

		for(int i = 1; i < dimensions.length; i++) {
			int[] remainingDims = Arrays.copyOfRange(dimensions, i, dimensions.length);
			if(remainingDims[0] != -1)
				for(int j = 0; j < dimensions[0]; j++)
					array.setElement(j, allocateArray(baseType, remainingDims));

		}
		objects.add(array);
		return array;
	}

	@Override
	public IRecord allocateRecord(IRecordType type) throws ExecutionError {
		Record object = new Record(type);
		objects.add(object);
		return object;
	}
}