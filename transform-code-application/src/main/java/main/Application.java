package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import original.BoxInterface;
import original.SquareBox;
import tool.ClassTransform;

public class Application {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NotFoundException, IOException, CannotCompileException {
			
			SquareBox b =  new SquareBox();  	
			
			System.out.println( b.getId() );

	}

}
