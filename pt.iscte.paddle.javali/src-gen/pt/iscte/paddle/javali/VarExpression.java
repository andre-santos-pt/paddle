/**
 * generated by Xtext 2.17.0
 */
package pt.iscte.paddle.javali;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link pt.iscte.paddle.javali.VarExpression#getParts <em>Parts</em>}</li>
 *   <li>{@link pt.iscte.paddle.javali.VarExpression#getArrayIndexes <em>Array Indexes</em>}</li>
 * </ul>
 *
 * @see pt.iscte.paddle.javali.JavaliPackage#getVarExpression()
 * @model
 * @generated
 */
public interface VarExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>Parts</b></em>' containment reference list.
   * The list contents are of type {@link pt.iscte.paddle.javali.Identifier}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parts</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parts</em>' containment reference list.
   * @see pt.iscte.paddle.javali.JavaliPackage#getVarExpression_Parts()
   * @model containment="true"
   * @generated
   */
  EList<Identifier> getParts();

  /**
   * Returns the value of the '<em><b>Array Indexes</b></em>' containment reference list.
   * The list contents are of type {@link pt.iscte.paddle.javali.Expression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Array Indexes</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Array Indexes</em>' containment reference list.
   * @see pt.iscte.paddle.javali.JavaliPackage#getVarExpression_ArrayIndexes()
   * @model containment="true"
   * @generated
   */
  EList<Expression> getArrayIndexes();

} // VarExpression
