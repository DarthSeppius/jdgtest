package org.jboss.as.quickstarts.datagrid.prova.model.custom;


import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.infinispan.commons.marshall.Externalizer;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;

public class RDExternalizer implements Externalizer<RawData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2427627692959596748L;

	@Override
	public RawData readObject(ObjectInput input) throws IOException, ClassNotFoundException {
		Long id = input.readLong();
		String halfKey = input.readUTF();
		String firstValue = input.readUTF();
		String secondValue = input.readUTF();
		String thirdValue = input.readUTF();
		String fileName = input.readUTF();
		RawData rd = new RawData();
		rd.setId(id);
		rd.setHalfKey(halfKey);
		rd.setFirstValue(firstValue);
		rd.setSecondValue(secondValue);
		rd.setThirdValue(thirdValue);
		rd.setFileName(fileName);
		return rd;
	}

	@Override
	public void writeObject(ObjectOutput output, RawData rd) throws IOException {
		output.writeLong(rd.getId());
		output.writeUTF(rd.getHalfKey());
		output.writeUTF(rd.getFirstValue());
		output.writeUTF(rd.getSecondValue());
		output.writeUTF(rd.getThirdValue());
		output.writeUTF(rd.getFileName());
	}

}
