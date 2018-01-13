package org.redshiftrobotics.lib;

public class Vector2D {
    private double xComponent;
    private double yComponent;


    public Vector2D(Vector2D vecVector) {
        this.xComponent = vecVector.getXComponent();
        this.yComponent = vecVector.getYComponent();
    }

    public Vector2D(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }

    public double getXComponent() {
        return xComponent;
    }

    public double getYComponent() {
        return yComponent;
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
    }

    public double getDirection() {
        return Math.atan2(yComponent, xComponent);
    }

    public void set(Vector2D newVector) {
        setComponents(newVector.getXComponent(), newVector.getYComponent());
    }

    public void setPolar(double magnitude, double direction) {
        xComponent = magnitude * Math.cos(direction);
        yComponent = magnitude * Math.sin(direction);
    }

    public void setComponents(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }

    public void add(Vector2D delta) {
        addComponents(delta.getXComponent(), delta.getYComponent());
    }

    public void addPolar(double magnitude, double direction) {
        xComponent += magnitude * Math.cos(direction);
        yComponent += magnitude * Math.sin(direction);
    }

    public void addComponents(double xComponent, double yComponent) {
        this.xComponent += xComponent;
        this.yComponent += yComponent;
    }

    public void multiply(Vector2D vector) {
        multiplyComponents(vector.getXComponent(), vector.getYComponent());
    }

    public void multiplyPolar(double magnitude, double direction) {
        xComponent *= magnitude * Math.cos(direction);
        yComponent *= magnitude * Math.sin(direction);
    }

    public void multiplyComponents(double xComponent, double yComponent) {
        this.xComponent *= xComponent;
        this.yComponent *= yComponent;
    }

    public double dotProduct(Vector2D vector) {
        return (xComponent * vector.getXComponent()) + (yComponent * vector.getYComponent());
    }

    public void multiplyScalar(double factor) {
        xComponent *= factor;
        yComponent *= factor;
    }
}
