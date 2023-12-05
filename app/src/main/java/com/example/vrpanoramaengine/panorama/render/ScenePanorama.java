package com.example.vrpanoramaengine.panorama.render;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.example.vrpanoramaengine.panorama.model.Arrow3D;
import com.example.vrpanoramaengine.panorama.model.Sphere3D;

import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

public class ScenePanorama {
    private final Sphere3D sphere;
    private final ArrayList<Arrow3D> arrow3dList;
    private final String name;
    public ScenePanorama(Sphere3D sphere, ArrayList<Arrow3D> arrow3dList, String name) {
        this.sphere = sphere;
        this.arrow3dList = arrow3dList;
        this.name = name;
    }
    public void rotateSceneAxisY(double radian) {
        double rotate = sphere.getRotX();
        double z = - cos(rotate + PI/2);
        double y = - sin(rotate + PI/2);

        sphere.rotate(new Vector3(0,y,z),-radian);

        arrow3dList.forEach(arrow3d -> {
            arrow3d.rotate(new Vector3(0,y,z),-radian);
        });
    }

    public void rotateSceneAxisXSpec(double radian) {
      //  double camRotX = sphere.getRotX();
     //   double boundAngle = PI / 2.1;

   //     if((camRotX - radian >= -boundAngle) && (camRotX - radian <= boundAngle) ) {
            sphere.rotate(Vector3.Axis.X, -radian);

            arrow3dList.forEach(arrow3d -> {
                arrow3d.rotate(Vector3.Axis.X, -radian);
            });
  /*      } else {
            if(camRotX - radian < 0) {
                sphere.rotate(Vector3.Axis.X, -(boundAngle) - camRotX);

                arrow3dList.forEach(arrow3d -> {
                    arrow3d.rotate(Vector3.Axis.X, -(boundAngle) - camRotX);
                });
            } else {
                sphere.rotate(Vector3.Axis.X, (boundAngle) - camRotX);

                arrow3dList.forEach(arrow3d -> {
                    arrow3d.rotate(Vector3.Axis.X, (boundAngle) - camRotX);
                });
            }
        }*/
    }


    public void rotateSceneAxisX(double radian) {
        double camRotX = sphere.getRotX();
        double boundAngle = PI / 2.1;

        if((camRotX - radian >= -boundAngle) && (camRotX - radian <= boundAngle) ) {
            sphere.rotate(Vector3.Axis.X, -radian);

            arrow3dList.forEach(arrow3d -> {
                arrow3d.rotate(Vector3.Axis.X, -radian);
            });
        } else {
            if(camRotX - radian < 0) {
                sphere.rotate(Vector3.Axis.X, -(boundAngle) - camRotX);

                arrow3dList.forEach(arrow3d -> {
                    arrow3d.rotate(Vector3.Axis.X, -(boundAngle) - camRotX);
                });
            } else {
                sphere.rotate(Vector3.Axis.X, (boundAngle) - camRotX);

                arrow3dList.forEach(arrow3d -> {
                    arrow3d.rotate(Vector3.Axis.X, (boundAngle) - camRotX);
                });
            }
        }
    }

    public void correctAngleDegree(double angle) {
        arrow3dList.forEach(arrow3d -> {
            arrow3d.setRotY(radianToDegree(sphere.getRotY()) + arrow3d.getAngle() + angle);
        });
    }

    private double radianToDegree(double radian) {
        return (radian * 360.0) / (2.0 * PI);
    }

    public Sphere3D getSphere() {
        return sphere;
    }

    public ArrayList<Arrow3D> getArrow3dList() {
        return arrow3dList;
    }
    public String getName() {return name;}
}
