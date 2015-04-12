package com.fpt.model;

public abstract class BaseDependency {
	public enum Scope{
		COMPILE("compile"), PROVIDED("provided");
		Scope(String text){
			this.text = text;
		}
		private String text;
		public String getText(){
			return text;
		}
	}
	protected Scope scope = Scope.COMPILE;
	
	public Scope getScope() {
		return scope;
	}
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
}
