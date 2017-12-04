/**
 * 
 */
package org.fortiss.platform;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.bean.DataBase;
import org.fortiss.platform.api.IToolchainManager;

/**
 * @author rajat
 *
 */
public class DBFactory {

	public static final Logger logger = LogManager.getLogger(DBFactory.class);
	private HashMap<DataBase, IToolchainManager> cachedMap = new HashMap<DataBase, IToolchainManager>();

	private static DBFactory dbFactory = null;

	private DBFactory() {
	}

	public static DBFactory getInstance() {
		if (dbFactory == null) {
			synchronized (DBFactory.class) {
				if (dbFactory == null) {
					dbFactory = new DBFactory();
				}
			}
		}
		return dbFactory;
	}

	public IToolchainManager getToolchainManager(DataBase dataBase)
			throws IOException {
		switch (dataBase) {

		case JSON:
			if (cachedMap.containsKey(dataBase)) {
				return cachedMap.get(dataBase);
			} else {
				JsonToolchainManager jsonDB = new JsonToolchainManager();
				cachedMap.put(dataBase, jsonDB);
				return jsonDB;
			}
		case MY_SQL:
			logger.error("Not yet implemented.");

		default:
			break;
		}
		return cachedMap.get(dataBase);
	}
}