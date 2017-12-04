/**
 * 
 */
package org.fortiss.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fortiss.exception.InvalidSignatureException;

/**
 * @author rajat
 *
 */
public class WorkflowSignature {

	private Map<String, List<String>> signatureOfToolsMap;

	public WorkflowSignature(String userEntry) throws InvalidSignatureException {
		// (eg. sa(m,m),mc(m,s),wp(o,m))
		signatureOfToolsMap = new HashMap<String, List<String>>();
		if (isSignatureGrammarCorrect(userEntry)) {
			String[] sign = userEntry.split("),");
			for (int i = 0; i < sign.length; i++) {
				String[] toolSignature = sign[i].split("(");
				String toolName = toolSignature[0];
				String[] toolSignatureInt = toolSignature[1].split(",");
				List<String> signList = new ArrayList<String>();
				signList.add(toolSignatureInt[0]);
				signList.add(toolSignatureInt[1]);
				signatureOfToolsMap.put(toolName, signList);
			}
		} else {
			throw new InvalidSignatureException(
					"Please check the signature again.\n [m = mandatory , o = optional]\n [m = multiple, s = single] ");
		}
	}

	private boolean isSignatureGrammarCorrect(String userEntry) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the signatureOfToolsList
	 */
	public Map<String, List<String>> getSignatureOfToolsList() {
		return signatureOfToolsMap;
	}

	/**
	 * @param signatureOfToolsList
	 *            the signatureOfToolsList to set
	 */
	public void setSignatureOfToolsList(
			Map<String, List<String>> signatureOfToolsList) {
		this.signatureOfToolsMap = signatureOfToolsList;
	}
}
