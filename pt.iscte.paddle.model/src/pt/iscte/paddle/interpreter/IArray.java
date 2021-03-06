package pt.iscte.paddle.interpreter;

import pt.iscte.paddle.model.IArrayType;

public interface IArray extends IValue {
	IArrayType getType();
	int getLength();
	IValue getElement(int i);
	void setElement(int i, IValue value);
	IArray copy();
	
	@Override
	default int getMemory() {
		return 4 + getLength() * getType().getMemoryBytes();
	}
	
	interface IListener {
		void elementChanged(int index, IValue oldValue, IValue newValue);
	}
}
