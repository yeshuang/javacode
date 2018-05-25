package com.summer.jvm;

/*
 * 虚拟机栈和本地方法栈溢出:
 * 	如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError异常。
 * VM Args：-Xss2M （这时候不妨设大些）
 */
public class StudyJavaVMStackOOM {
	private void dontStop() {
		while (true) {
		}
	}

	public void stackLeakByThread() {
		while (true) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					dontStop();
				}
			});
			thread.start();
		}
	}

	public static void main(String[] args) throws Throwable {
		StudyJavaVMStackOOM oom = new StudyJavaVMStackOOM();
		oom.stackLeakByThread();
	}

}
