from vertexvista.utils import matching_symbols
import jedi
 
class PyNav():
    def __init__(self, root, num_workers=16):
        self.project = jedi.Project(root)
 
    def execute(self, file, line, column):
        with open(file, "r") as f:
            doc = f.read()
        script = jedi.Script(doc, project=self.project)
        definitions = script.goto(line+1, column, follow_imports=True)
        return definitions
 
    def matching(self, definitions, cached_symbols):
        if definitions[0].module_path in cached_symbols:
            symbols = cached_symbols[definitions[0].module_path]
            symbol = matching_symbols(symbols, definitions[0])
        else:
            symbol = None
        return symbol