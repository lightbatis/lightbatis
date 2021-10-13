/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package titan.lightbatis.web.entity.propertyset;

/**
 * Thrown if a property is attempted to be retrieved that
 * does exist but is of different type.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 146 $
 */
public class InvalidPropertyTypeException extends PropertyException {
  //~ Constructors ///////////////////////////////////////////////////////////

  public InvalidPropertyTypeException() {
    super();
  }

  public InvalidPropertyTypeException(String msg) {
    super(msg);
  }
}
