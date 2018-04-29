package org.mdground.api.base;

public enum ResponseCode {

	Normal(0), 
	SystemError(0x1), 
	ApplicationError(0x2), 
	Warning(0x4), 
	VersonNotSpported(0x1 | 0x8), 
	LanguageNotSpported(0x4 | 0x10), 
	RequestCodeNotSupported(0x1 | 0x20), 
	LoadModuleFailed(0x1 | 0x40), 
	AppUnhandledException(0x1 | 0x2 | 0x80), 
	InvalidToken(0x100), 
	InvalidDevice(0x200), 
	NewNotify(0x400), 
	AppCustom0(0x80000);

	private int value;

	private ResponseCode(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
