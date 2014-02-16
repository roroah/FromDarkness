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
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Bob
 */
public class Player extends AbstractAppState {
   
public  SimpleApplication      app;
public  AppStateManager        stateManager;
public  AssetManager           assetManager;
public  BulletAppState         physics;
public  Node                   rootNode;
public  Node                   Model;
public  BetterCharacterControl playerControl;

public  ArrayList              inventory;
public  String                 heldItem;
public  Player                 player;
public  int                    playerHealth;



  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app          = (SimpleApplication) app; // can cast Application to something more specific
    this.rootNode     = this.app.getRootNode();
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.physics      = this.stateManager.getState(BulletAppState.class);
    initPlayer();
    
   
   }
  

  
  
    public void initPlayer() {
      player = new Player();
      player.inventory = new ArrayList<String>();
       
       player.playerControl = new BetterCharacterControl(1f, 5f, 1f);
      
       player.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
       player.playerHealth = 20;
       player.Model.setLocalTranslation(0f, 0f, 0f);
       player.Model.setLocalScale(.7f);
       player.Model.addControl(player.playerControl);

       player.playerControl.setGravity(new Vector3f(0f,-9.81f,0f));
       player.playerControl.setJumpForce(new Vector3f(0f,5f,0f));
  
       physics.getPhysicsSpace().add(player.playerControl);
       rootNode.attachChild(player.Model);
       System.out.println("Player State Attached");
       
    }
    
    
    public int getHealth(Player player){
      return player.playerHealth;
      }
    
    public void changeHealth(Player player, int change){
      int currentHealth = player.getHealth(player);
      player.playerHealth = currentHealth + change;
      }
    
    
    public String getItemInHand() {
      return heldItem;
    }
    
    
    
    public void setItemInHand(String item, Player player) {
      try {
      inventory.remove(item);
      inventory.add(heldItem);
      heldItem = item;

      } catch (NullPointerException e) {
      System.out.println("Null in the hand");
      }
    }
    
    
    
    public void grabItem(Node shootables, Camera cam,Player player) {
       CollisionResults grabResults = new CollisionResults();
       Ray grabRay = new Ray(cam.getLocation(), cam.getDirection());
       shootables.collideWith(grabRay, grabResults);
       String grabbedItem;
       System.out.println(player.Model.getChildren());
 
       if (grabResults.size() > 0) {
         grabbedItem = grabResults.getCollision(0).getGeometry().getName();

         } else {
         grabbedItem = "air";
         }
        
        if(grabbedItem.equals("Billy")){
          player.inventoryAddItem(grabbedItem, player);
          player.Model.attachChild(grabResults.getCollision(0).getGeometry());
          grabResults.getCollision(0).getGeometry().setLocalTranslation(0, -10, 0);
          }
        
        if(grabbedItem.equals("Gun")){
          player.inventoryAddItem(grabbedItem, player);
          System.out.println("That's a gun!");
          CollisionResult grabbed = grabResults.getCollision(0);
          player.Model.attachChild(grabbed.getGeometry());
          grabbed.getGeometry().setLocalTranslation(0, -10, 0);
          
          }      
    }
   
    
    
    public ArrayList getInventory(Player player, GUI GUI) {
      GUI.inventoryWindow(player, GUI);
      return player.inventory;
      }
    
    
    
    public void inventoryAddItem(String item, Player player) {
      int inventoryLimit = 10;
      if(player.inventory.size() < inventoryLimit){
        player.inventory.add(item);
         System.out.println("added " + item + " to " + player);

        } else {
        System.out.println("There is no more room in your inventory" + player.inventory.size());
        }
    
    }
    
    
    public void Attack(Camera cam, Player player, AnimationAppState animInteract, String legAnim, Node monsterNode){
        
        int range;
        int damage;
        
        try {

        if(player.getItemInHand().equals("Gun")){
          System.out.println("Shootbang!");
          range = 20;
          damage = 5;

          } else {
          System.out.println("Puncharoonie!");
          String armAnim = "Punch";
          range = 4;
          damage = 1;
          animInteract.animChange(armAnim, legAnim, player.Model);
          }

       } catch (NullPointerException e) {
          System.out.println("Puncharoonie!");
          String armAnim = "Punch";
          range = 4;
          damage = 1;
          animInteract.animChange(armAnim, legAnim, player.Model);
       }
        
       CollisionResults attackResults = new CollisionResults();
       Ray attackRay = new Ray(cam.getLocation(), cam.getDirection());
       monsterNode.collideWith(attackRay, attackResults);
       
       for(int i = 0; i < attackResults.size(); i++) {
         System.out.println(attackResults.getCollision(i).getGeometry().getParent().getParent());
         }
       }
    
    public void Jump(BetterCharacterControl playerControl){
        playerControl.jump();
    }
    
}
