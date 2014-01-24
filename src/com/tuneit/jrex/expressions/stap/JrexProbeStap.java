package com.tuneit.jrex.expressions.stap;

import com.tuneit.jrex.expressions.JrexExpression;
import com.tuneit.jrex.expressions.JrexStatement;
import com.tuneit.jrex.expressions.JrexStmBody;
import com.tuneit.jrex.expressions.JrexExprLogical;

public class JrexProbeStap implements JrexExpression {
	public JrexProbeStap(String name, JrexExpression predicate,
			JrexStmBody body) {
		this.name = name;
		this.predicate = predicate;
		this.body = body;
	}

	private String name;
	private JrexExpression predicate;
	private JrexStmBody body;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.name);
		sb.append("\n{ \n");
		
		if(this.predicate != null) {
			/* In SystemTap we use if(!predicate) next; */
			JrexExpression invPredicate = 
					new JrexExprLogical(JrexExprLogical.NOT, this.predicate, null);
			
			sb.append("\nif(");
			sb.append(invPredicate.toString());
			sb.append(") next; \n");
		}
		
		sb.append(this.body.toString());
		
		sb.append("\n} \n\n");
		
		return sb.toString();
	}

}
