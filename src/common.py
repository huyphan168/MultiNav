from vertexvista.MultiNav.src.java.interface import JavaNav
from vertexvista.MultiNav.src.python.interface import PyNav
    
class MultiNav:
    def __init__(self, language, root):
        if language == "python":
            self.nav = PyNav(root)
        elif language == "java":
            self.nav = JavaNav(root)
    
    def request_definition(self, file_path, line, column):
        result = self.nav.execute(file_path, line, column)
        return result