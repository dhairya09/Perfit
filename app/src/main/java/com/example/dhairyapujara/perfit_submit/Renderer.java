package com.example.dhairyapujara.perfit_submit;

/**
 * Created by Dhairya Pujara on 27-04-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.rajawali3d.Object3D;
import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.math.vector.Vector3;




/**
 * Created by clintonmedbery on 4/6/15.
 */
public class Renderer extends RajawaliRenderer {

    public Context context;
    private PointLight mLight;
    private Object3D mObjectGroup;

    //private CamaraActualizada arcballCamera;

    private DirectionalLight directionalLight;
    private Sphere earthSphere;

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);

    }

    @Override
    protected void initScene() {

       /* mLight = new PointLight();
        mLight.setPosition(4, 4, 4);
        mLight.setPower(4);
        mLight.setScale(50f);

        getCurrentScene().addLight(mLight);
        getCurrentCamera().setZ(16);*/

        DirectionalLight light = new DirectionalLight();
        light.setLookAt(1, -1, 1);
        light.enableLookAt();
        light.setPower(1.25f);
        getCurrentScene().addLight(light);

        light = new DirectionalLight();
        light.setLookAt(-1, 1, -1);
        light.enableLookAt();
        light.setPower(1.25f);
        getCurrentScene().addLight(light);

        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
                mTextureManager, R.raw.chr_obj);
        try {
            objParser.parse();
            mObjectGroup = objParser.getParsedObject();
            mObjectGroup.setScale(2.5f);

            getCurrentScene().addChild(mObjectGroup);


            ArcballCamera arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.rajawali_surface));
            arcball.setTarget(mObjectGroup); //your 3D Object

            arcball.setPosition(4,4,4); //optional

            getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);

				/*mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
				mCameraAnim.setDurationMilliseconds(8000);
				mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
				mCameraAnim.setTransformable3D(mObjectGroup);*/
        } catch (ParsingException e) {
            e.printStackTrace();
        }

			/*mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
					new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
					360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

			mLightAnim.setDurationMilliseconds(3000);
			mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
			mLightAnim.setTransformable3D(mLight);

			getCurrentScene().registerAnimation(mCameraAnim);
			getCurrentScene().registerAnimation(mLightAnim);

			mCameraAnim.play();
			mLightAnim.play();*/
    }



    /*@Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        earthSphere.rotate(Vector3.Axis.Y, 1.0);
    }*/


    public void onTouchEvent(MotionEvent event){


    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }
}