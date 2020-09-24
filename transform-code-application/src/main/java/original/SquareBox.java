package original;

import tool.AddConstructor;

@AddConstructor
public class SquareBox implements BoxInterface {

	private int id;
	private String name;
	public String test;

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
