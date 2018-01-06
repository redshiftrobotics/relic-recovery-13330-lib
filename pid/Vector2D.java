package org.redshiftrobotics.lib.pid;

/**
 * Created by NoahR on 9/16/17.
 */

public class Vector2D {
    private double m_nXComponent;
    private double m_nYComponent;


    public Vector2D(Vector2D vecVector) {
        this.m_nXComponent = vecVector.getXComponent();
        this.m_nYComponent = vecVector.getYComponent();
    }

    public Vector2D(double nXComponent, double nYComponent) {
        m_nXComponent = nXComponent;
        m_nYComponent = nYComponent;
    }

    public double getXComponent() {
        return m_nXComponent;
    }

    public double getYComponent() {
        return m_nYComponent;
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(m_nXComponent, 2) + Math.pow(m_nYComponent, 2));
    }

    public double getDirection() {
        return Math.atan2(m_nYComponent, m_nXComponent);
    }

    public void set(Vector2D vNewVector) {
        this.setComponents(vNewVector.getXComponent(), vNewVector.getYComponent());
    }

    public void setPolar(double nMagnitude, double nDirection) {
        m_nXComponent = nMagnitude * Math.cos(nDirection);
        m_nYComponent = nMagnitude * Math.sin(nDirection);
    }

    public void setComponents(double nXComponent, double nYComponent) {
        m_nXComponent = nXComponent;
        m_nYComponent = nYComponent;
    }

    public void add(Vector2D vDelta) {
        this.addComponents(vDelta.getXComponent(), vDelta.getYComponent());
    }

    public void addPolar(double nMagnitude, double nDirection) {
        m_nXComponent += nMagnitude * Math.cos(nDirection);
        m_nYComponent += nMagnitude * Math.sin(nDirection);
    }

    public void addComponents(double nXComponent, double nYComponent) {
        m_nXComponent += nXComponent;
        m_nYComponent += nYComponent;
    }

    public void multiply(Vector2D vVector) {
        this.multiplyComponents(vVector.getXComponent(), vVector.getYComponent());
    }

    public void multiplyPolar(double nMagnitude, double nDirection) {
        m_nXComponent *= nMagnitude * Math.cos(nDirection);
        m_nYComponent *= nMagnitude * Math.sin(nDirection);
    }

    public void multiplyComponents(double nXComponent, double nYComponent) {
        m_nXComponent *= nXComponent;
        m_nYComponent *= nYComponent;
    }

    public double dotProduct(Vector2D vVector) {
        return (m_nXComponent * vVector.getXComponent()) + (m_nYComponent * vVector.getYComponent());
    }

    public void multiplyScalar(double nFactor) {
        m_nXComponent *= nFactor;
        m_nYComponent *= nFactor;
    }
}
