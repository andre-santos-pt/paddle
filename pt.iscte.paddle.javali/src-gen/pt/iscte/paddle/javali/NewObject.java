/**
 * generated by Xtext 2.17.0
 */
package pt.iscte.paddle.javali;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>New Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link pt.iscte.paddle.javali.NewObject#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see pt.iscte.paddle.javali.JavaliPackage#getNewObject()
 * @model
 * @generated
 */
public interface NewObject extends Expression
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' containment reference.
   * @see #setType(Identifier)
   * @see pt.iscte.paddle.javali.JavaliPackage#getNewObject_Type()
   * @model containment="true"
   * @generated
   */
  Identifier getType();

  /**
   * Sets the value of the '{@link pt.iscte.paddle.javali.NewObject#getType <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' containment reference.
   * @see #getType()
   * @generated
   */
  void setType(Identifier value);

} // NewObject
