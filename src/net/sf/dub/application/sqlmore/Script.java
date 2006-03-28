package net.sf.dub.application.sqlmore;

import java.io.*;

/**
 * SQL script handler
 */
public class Script extends Parser {

	FileInputStream m_file_in;

	public Script(String p_name) {
		try {

			File v_file = new File(p_name);
			// check to see if the dir already exists
			if (v_file.exists() == false) {
				printerr("Unable to locate file: " + p_name); //$NON-NLS-1$
				return;
			}

			// not needed but might be interesting for optimizing the bufferer
			// long v_size = v_file.length();
			// Console.println("File size: "+v_size);

			if (!v_file.canRead()) {
				printerr("Unable to read from file: " + p_name); //$NON-NLS-1$
				return;
			}

			m_file_in = new FileInputStream(p_name);
			//
			// setup a buffered reader to read and execute
			//
			init(m_file_in);
		}
		catch (IOException e_io) {
			printerr("Read from " + p_name + " failed");  //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	public void deinit() {
		try {
			if (m_file_in != null)
				m_file_in.close();
		}
		catch (IOException e_io) {
		}
	}

	public void finalize() {
		deinit();
	}

}
