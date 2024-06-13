package runPy;

public class runPyfileImpl implements runPyfile {
    // 일단은 절대경로로 지정
    String mlbPypath = "";
    String kboPypath = "";

    @Override
    public void runMLBpy(String mlbPypath) {
    	pyRunner(mlbPypath);
    }

    @Override
    public void runKBOpy(String kboPypath) {
        pyRunner(kboPypath);
    }

	@Override
	public void pyRunner(String path) {
		try {
			ProcessBuilder pb =new ProcessBuilder("python", path);
			pb.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
