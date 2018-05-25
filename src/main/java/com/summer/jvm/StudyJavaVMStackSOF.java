package com.summer.jvm;

/*
 * 虚拟机栈和本地方法栈溢出:
 * 	如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常。
 * VM Args：-Xss160k
 */
public class StudyJavaVMStackSOF {
	private int stackLength = 1;

	public void stackLeak() {
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) throws Throwable {
		StudyJavaVMStackSOF oom = new StudyJavaVMStackSOF();
		try {
			oom.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack length:" + oom.stackLength);
			throw e;
		}
	}
}
