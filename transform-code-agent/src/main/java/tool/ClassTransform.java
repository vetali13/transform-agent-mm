package tool;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

public class ClassTransform {
	
	public static void premain( String args, Instrumentation instrumentation ) {
		
		System.out.println("Test Agent RUNNING");
		
		instrumentation.addTransformer(new ClassFileTransformer () {
			@Override
			public byte[] transform(Module module,
									ClassLoader loader,
									String name,
									Class<?> typeIfLoaded,
									ProtectionDomain domain,
									byte[] buffer) {
				
				// Get the class manipulation tool = pool
				ClassPool pool = ClassPool.getDefault();
				
				// in case the classes are not found
				pool.appendClassPath(new LoaderClassPath(loader));
				
				if (name.contains("original")) {
					
					try {
						
						// load the desired class
						CtClass cc = pool.get(name.replace("/", "."));
						
						if (!cc.isInterface() && !Modifier.isAbstract( cc.getModifiers()) && cc.hasAnnotation(AddConstructor.class)) {
							
							ArrayList<String> fieldTypesAndNames = new ArrayList<>();
							ArrayList<String> fieldNames = new ArrayList<>();
							
							for (CtField ctField : cc.getDeclaredFields()) {
								if ((ctField.getModifiers() & Modifier.PRIVATE) != 0) {
									fieldTypesAndNames.add( ctField.getType().getName() + " " + ctField.getName());
									fieldNames.add(ctField.getName());
								}
							}
							
							String arguments = String.join(", ", fieldTypesAndNames);
							String body = "";
							
							for (String fieldName : fieldNames) {
								body += "this." + fieldName + " = " + fieldName + ";\n";
							}
							
							CtConstructor cstructor = CtNewConstructor.make(
									"public " + cc.getName().split("\\.")[1] + "(" +
									arguments + ")" + "{\n" +
									body + "}", 
									cc);
							
							// print the installed constructor
							System.out.println("public " + cc.getName().split("\\.")[1] + "(" +
									arguments + ")" + "{\n" +
									body + "}");
							
							// return the modified class
							return cc.toBytecode();
							
						}
					} catch (NotFoundException | IOException | CannotCompileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
						
				}
						
				
				return null;
			}
			
			
		});
		
	}
	
}
