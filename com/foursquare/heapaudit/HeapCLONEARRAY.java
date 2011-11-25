package com.foursquare.heapaudit;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;

class HeapCLONEARRAY extends HeapAudit {

    // Allocations by the clone method on any array type are triggered via calls
    // to visitMethodInsn(INVOKEVIRTUAL) where it returns a reference to the
    // newly allocated array object.

    public static void after(boolean debug,
			     boolean trace,
			     MethodAdapter mv,
			     String owner) {

	instrumentation(debug,
			"\tCLONEARRAY.after");

	execution(trace,
		  mv,
		  "\tCLONEARRAY.after");

	// STACK: [...|obj]
	mv.visitInsn(Opcodes.DUP);
	// STACK: [...|obj|obj]
	mv.visitTypeInsn(Opcodes.CHECKCAST,
			 owner);
	// STACK: [...|obj|obj]

	if (owner.charAt(1) != '[') {                                                                                                                                                      

	    // STACK: [...|obj|obj]
	    mv.visitInsn(Opcodes.DUP);
	    // STACK: [...|obj|obj|obj]
	    mv.visitInsn(Opcodes.ARRAYLENGTH);
	    // STACK: [...|obj|obj|count]
	    mv.visitLdcInsn(owner.substring(1));
	    // STACK: [...|obj|obj|count|type]
	    mv.visitLdcInsn((long)-1);
	    // STACK: [...|obj|obj|count|type|size]
	    mv.visitMethodInsn(Opcodes.INVOKESTATIC,
			       "com/foursquare/heapaudit/HeapAudit",
			       "record",
			       "(Ljava/lang/Object;ILjava/lang/String;J)V");
	    // STACK: [...|obj]

	}
	else {

	    // STACK: [...|obj|obj]
	    mv.visitLdcInsn(owner);
	    // STACK: [...|obj|obj|type]
	    mv.visitMethodInsn(Opcodes.INVOKESTATIC,
			       "com/foursquare/heapaudit/HeapAudit",
			       "record",
			       "(Ljava/lang/Object;Ljava/lang/String;)V");
	    // STACK: [...|obj]

	}

    }

}
