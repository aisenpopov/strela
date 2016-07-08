package ru.strela.util;

import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SystemException extends Error {
    private static final Logger s_logger = Logger.getLogger(SystemException.class.getName());
    // when throwing exception across RPC we keep stack trace here
    private final List<String> o_stackTrace;

    /**
     * Creates new <code>SystemException</code> without detail message.
     */
    public SystemException() {
        this("(no description)");
    }

    /**
     * Constructs an <code>SystemException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SystemException(String msg) {
        super(msg);
        o_stackTrace = splitIntoStrings(serializeStack(this));
    }

    public SystemException(Throwable t) {
        this("", t);
    }

    /**
     * Constructs an <code>SystemException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SystemException(String msg, Throwable th) {
        super(msg, th);
        List<String> olderStack = splitIntoStrings(serializeStack(th));
        List<String> myStack = splitIntoStrings(serializeStack(this));
        List<String> integratedStack = new ArrayList<String>();
        integratedStack.add(olderStack.get(0));
        for (int i = 1; i < olderStack.size(); i++) {
            String s = olderStack.get(i);
            if (myStack.contains(s)) break;
            integratedStack.add(s);
        }
        String myMsg = myStack.get(0);
        integratedStack.add("    Converted to [" + myMsg + "]");
        for (int i = 1; i < myStack.size(); i++) {
            String s = myStack.get(i);
            integratedStack.add(s);
        }
        o_stackTrace = integratedStack;
    }

    public void printStackTrace(PrintWriter p_writer) {
        if (o_stackTrace == null) {  // when called from constructor
            super.printStackTrace(p_writer);
        } else {
            for (String line: o_stackTrace) {
                p_writer.println(line);
            }
        }
    }

    public void printStackTrace(PrintStream p_writer) {
        printStackTrace(new PrintWriter(p_writer, true));
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public static String serializeStack(Throwable th) {
        StringWriter w = new StringWriter();
        th.printStackTrace(new PrintWriter(w, true));
        return w.toString();
    }

    public static List<String> splitIntoStrings(String s) {
        List<String> list = new ArrayList<String>();
        StringTokenizer tok = new StringTokenizer(s, "\n", false);
        for (; tok.hasMoreTokens();) {
            String line = tok.nextToken();
            while (line.length() > 0 && line.charAt(line.length() - 1) <= ' ')
                line = line.substring(0, line.length() - 1);
            list.add(line);
        }
        return list;
    }

    public static SystemException processException(String p_msg, Throwable p_ex) {
        s_logger.error(p_msg, p_ex);
        return (p_ex instanceof SystemException) ? (SystemException) p_ex : new SystemException(p_msg, p_ex);
    }

    public static SystemException processException(Throwable p_ex) {
        return SystemException.processException("", p_ex);
    }

    public static SystemException processUnwrapped(RemoteException e) {
        Throwable t = e.getCause();
        Throwable tt = (t != null) ? t.getCause() : null;
        if (tt == null) tt = t;

        return SystemException.processException(tt == null ? e : tt);
    }
}
