import jpype
import jpype.imports

class JavaNav:
    def __init__(self, root, max_workers=16):
        jpype.startJVM(classpath=["/datadrive05/huypn16/VertexVista/src/vertexvista/MultiNav/src/java/target/multinav-1.0-SNAPSHOT-jar-with-dependencies.jar"])
        java_nav_native = jpype.JClass('com.fsoft.multinav.MultiNav')
        self.multinav = java_nav_native(root)
        
    def execute(self, file, line, column):
        result_str = self.multinav.execute(file, line, column)
        return {"path": result_str.split(":")[0], "line": int(result_str.split(":")[1])}

class ParallelJavaNav:
    def __init__(self, root, max_workers=16):
        jpype.startJVM(classpath=["src/java/target/multinav-1.0-SNAPSHOT-jar-with-dependencies.jar"])
        java_nav_native = jpype.JClass('com.fsoft.multinav.ParallelMultiNav')
        self.multinav = java_nav_native(root)
        
    def execute(self, py_tasks):
        java_task_cls = jpype.JClass("com.fsoft.multinav.Task")
        # Convert Python list to Java List
        java_tasks = jpype.JClass("java.util.ArrayList")()
        for task in py_tasks:
            java_task = java_task_cls(task[0], task[1], task[2])
            java_tasks.add(java_task)
        
        # Call the Java execute method
        java_results = self.multinav.execute(java_tasks)
        
        # Convert Java List to Python list
        results = []
        for java_result in java_results:
            results.append(str(java_result))
        
        return results

if __name__ == "__main__":
    import time
    nav = ParallelJavaNav("/home/anhnt446/MultiNav/src/java/src/test/examples")
    start = time.time()
    results = nav.execute([("/org/javacs/example/Goto.java", 17,14) for i in range(10000)])
    print(time.time() - start)
    print(results)