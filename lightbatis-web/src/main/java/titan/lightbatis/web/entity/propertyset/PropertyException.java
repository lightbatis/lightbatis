/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package titan.lightbatis.web.entity.propertyset;

/**
 * Parent class of all exceptions thrown by PropertySet.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 146 $
 */
public class PropertyException extends RuntimeException {
  //~ Constructors ///////////////////////////////////////////////////////////

  public PropertyException() {
    super();
  }

  public PropertyException(String msg) {
    super(msg);
  }
}
