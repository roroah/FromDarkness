/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {
    
private SimpleApplication app;
private Node              rootNode;
private AssetManager      assetManager;
private AppStateManager   stateManager;
private BulletAppState    physics;

public  Node              grabbable;

public Node               sceneModel;
public Node                Model;

private Material          mat;

    
      @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app          = (SimpleApplication) app; // can cast Application to something more specific
    this.rootNode     = this.app.getRootNode();
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.physics      = this.stateManager.getState(BulletAppState.class);
    
    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.randomColor());
    grabbable = new Node("Grabbables");
    rootNode.attachChild(grabbable);
    grabbable.attachChild(initScene());
    grabbable.attachChild(initGun());   
    grabbable.attachChild(makeAmmo("Ammo", 5f, 5f));
    grabbable.attachChild(makeHealth("Health", -5f, -5f));
    
    }
      
    public Spatial initScene(){
          
        sceneModel = (Node) assetManager.loadModel("Scenes/Bob'sTestCell.j3o");
        sceneModel.setLocalScale(2);
        CollisionShape sceneShape =
        CollisionShapeFactory.createDynamicMeshShape(sceneModel);
        RigidBodyControl scenePhys = new RigidBodyControl(sceneShape);
        sceneModel.addControl(scenePhys);
        sceneModel.setLocalTranslation(5f, 0f, 1f);
        scenePhys.setMass(0f);
        physics.getPhysicsSpace().add(scenePhys);
        System.out.println("Scene Initialized");
        return sceneModel;
        }
   
  /** A cube object for target practice */
  public Geometry makeAmmo(String name, float x, float z) {
    Box box = new Box(1, 1, 1);
    Geometry cube = new Geometry(name, box);
    cube.setLocalTranslation(x, 1, z);
    cube.setMaterial(mat);
    return cube;
    }

  public Geometry makeHealth(String name, float x, float z) {
    Box box = new Box(1, 1, 1);
    Geometry cube = new Geometry(name, box);
    cube.setLocalTranslation(x, 1, z);
    cube.setMaterial(mat);
    return cube;
    }
    
    protected Spatial initGun(){   
      Spatial gun = assetManager.loadModel("Models/Gun/Gun.j3o");
      gun.setLocalTranslation(85f, 0f, 15f);
      gun.setLocalScale(.3f);
      System.out.println("Gun Initialized");
      return gun;
      }

  
    
}
