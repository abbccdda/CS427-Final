package edu.ncsu.csc.itrust.enums;

public enum DeliveryMethod {
	Vaginal_Delivery("Vaginal Delivery"), Caesarean_Section("Caesarean Section"), Miscarriage("Miscarriage"),
	NS("N/S");
	
	private String name;

	private DeliveryMethod(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static DeliveryMethod parse(String deliveryMethodStr) {
		for (DeliveryMethod type : DeliveryMethod.values()) {
			if (type.getName().equals(deliveryMethodStr)) {
				return type;
			}
		}
		return NS;
	}
}
