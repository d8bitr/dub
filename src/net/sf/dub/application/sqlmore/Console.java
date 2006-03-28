package net.sf.dub.application.sqlmore;

import java.io.*;



/**
 * Console I/O class with workaround for line buffer in Java console mode
 *
 * This class adds JNI based hotkey functionality to supported platforms. The 
 * idea is to enable tab completeion, directional keys, and function keys.
 * Platforms without a JNI library will simply lack the hotkey functionality
 * but be otherwise fully functional.
 *
 * NOTE: I wanted to implement key sensitive actions but Java's System.in
 * absolutely requires an <ENTER> before forwarding the keyboard buffer.
 *
 * Copyright (c) 2001-2004 Marc Lavergne. All rights reserved.
 *
 * The following products are free software; Licensee can redistribute and/or 
 * modify them under the terms of	the GNU Lesser General Public License
 * (http://www.gnu.org/copyleft/lesser.html) as published by the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-	1307  USA; 
 * either version 2.1 of the	license, or any later version:
 */
public class Console {
	
	private Writer writer;
	private PrintWriter print;
	
	public void setWriter(Writer writer) {
		if (writer != null) {
			this.writer = writer;
			print = new PrintWriter(writer);
		}
	}
	
	public Writer getWriter() {
		return writer;
	}

	private StreamTokenizer tokenizer;

	public void println() {
		if (print != null) {
			print.println();
			print.flush();
		}
	}

	public void println(String p_string) {
		if (print != null) {
			print.println(p_string);
			print.flush();
		}
	}

	public void print(String p_string) {
		if (print != null) {
			print.print(p_string);
			print.flush();
		}
	}

	public void printerr(String p_string) {
		if (print != null) {
			print.println(p_string);
			print.flush();
		}
	}

	protected void init(InputStream p_stream) {
		//  open up stdin
		tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(p_stream)));
		tokenizer.resetSyntax();
		//      m_st.wordChars((char)0, (char)255);
		//      m_st.whitespaceChars('\u0000', '\u0020');
		//      m_st.commentChar('-');
		//      m_st.slashStarComments(true);
		tokenizer.eolIsSignificant(true);
	};

	protected String readLine() {
		// leave this as an int, using a char causes some funky casting of the stream terminators
		int v_char;
		StringBuffer v_line = new StringBuffer();
		if (tokenizer == null)
			return ""; //$NON-NLS-1$

		try {
			do {
				v_char = tokenizer.nextToken();
				v_line.append((char)v_char);
			}
			while(v_char != StreamTokenizer.TT_EOL && v_char != StreamTokenizer.TT_EOF);

			if (v_char == StreamTokenizer.TT_EOF) {
				// this is tricky since chars in java can't be signed, we check for
				// the return 65535 as a workaround
				return String.valueOf((char)StreamTokenizer.TT_EOF);
			}
			return v_line.toString().trim();
		}
		catch (IOException e_io) {
			System.err.println(e_io.toString());
			System.exit(1);
			return ""; //$NON-NLS-1$
		}
	}

}
