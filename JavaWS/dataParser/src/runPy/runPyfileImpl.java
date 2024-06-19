package runPy;

public class runPyfileImpl implements runPyfile {
    @Override
    public void runMLBpy(String path) {
    	pyRunner(path);
    }

    @Override
    public void runKBOpy(String path) {
        pyRunner(path);
    }
    
	@Override
	public void runMLBresultpy(String path) {
		pyRunner(path);
	}

	@Override
	public void runKBOresultpy(String path) {
		pyRunner(path);
	}
	
	@Override
	public void pyRunner(String path) {
		try {
			ProcessBuilder pb =new ProcessBuilder("python", path);
			System.out.println("pyFile Path : " + path);
			pb.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
