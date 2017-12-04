/**
 * 
 */
package org.fortiss.platform.api;

import java.io.IOException;
import java.util.List;

/**
 * @author rajat
 *
 */
public interface IDataBase {

	public <T> Integer createRecord(T record) throws IOException;

	public <T> Integer updateRecord(Integer recordId, T record);

	public <T> boolean deleteRecord(Integer recordId, T clazz)
			throws IOException;

	public <T> void deleteRecords(List<Integer> listRecordIds, T clazz)
			throws IOException;

	public <T> T getRecord(Integer recordId, T clazz) throws IOException;

	public <T> List<T> getRecords(T clazz) throws IOException;

}
