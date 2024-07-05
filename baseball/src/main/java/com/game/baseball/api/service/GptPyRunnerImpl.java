package com.game.baseball.api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.game.baseball.api.config.FilePathsUtil;
import com.game.baseball.api.dao.BaseBallDaoImpl;
import com.game.baseball.api.jsonFileManager.readManager.readJSONImpl;
import com.game.baseball.api.runPy.runPyfileImpl;

@Service
public class GptPyRunnerImpl implements PyRunner{

	private static final Logger logger = LoggerFactory.getLogger(GptPyRunnerImpl.class);
	
	@Autowired
    private BaseBallDaoImpl bbd;
	
    @Autowired
    private readJSONImpl rji;

    @Autowired
    private runPyfileImpl rpi;
    
    @Autowired
    private FilePathsUtil filePathsUtil;
    
    
	@Override
	@Transactional
	public void pyRunner() {
		String gptPyPath = filePathsUtil.getGptExecptPyPath();
		
		logger.info("Running GPT exepect script at path: {}", gptPyPath);
		rpi.pyRunner(gptPyPath);
		logger.info("GPT exepect script completed");
        
        List<Map<String, Object>> elist = rji.readGPTExecpet();
		
        logger.info("Dropping and recreating Gpt exepect table");
		bbd.dropGPTAnlytbl();
		bbd.createTemporaryGPTAnly();
		
		if(elist != null && !elist.isEmpty()) {
			logger.info("Insert GTP Exepect result Data");
			bbd.insertTdyExepect(elist);
		}else {
			logger.info("No GTP Exepect data to insert");
		}
		
		logger.info("Moving GTP Exepect data to main table");
		bbd.moveTempGPTexepectToMainTable();
	}

}
