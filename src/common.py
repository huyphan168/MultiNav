class BaseNav:
    def ___init__(self, root, max_workers=16):
        self.root = root
        self.max_workers = max_workers

    def execute(self, file, line, column):
        raise NotImplementedError("Subclasses should implement this!")
    
    
