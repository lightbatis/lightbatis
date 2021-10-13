/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package titan.lightbatis.web.entity.propertyset;

/**
 * Thrown if a property is set which is not allowed.
 * <p/>
 * <p><i>e.g.</i> non-serializable Object is passed to SerializablePropertySet,
 * or field is persisted that cannot be stored in database.</p>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 146 $
 */
public class IllegalPropertyException extends PropertyException {
  //~ Constructors ///////////////////////////////////////////////////////////

  public IllegalPropertyException() {
    super();
  }

  public IllegalPropertyException(String msg) {
    super(msg);
  }
}
