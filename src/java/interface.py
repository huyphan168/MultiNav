import jpype
import jpype.imports
from jpype.types import JArray
from src.common import BaseNav

class JavaNav(BaseNav):
    def __init__(self, root, max_workers=16):
        super().__init__(root, max_workers)
        jpype.startJVM(classpath=["target/multinav-1.0-SNAPSHOT-jar-with-dependencies.jar"])
        java_nav_native = jpype.JClass('com.fsoft.multinav.MultiNav')
        self.multinav = java_nav_native(root)
        
    def execute(self, file, line, column):
        return self.multinav.execute(file, line, column)