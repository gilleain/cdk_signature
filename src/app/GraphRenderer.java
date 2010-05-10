package app;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.deterministic.Graph;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;


public class GraphRenderer {
    
  private static int thumbNodeRadius = 3;
  
  private static int largeNodeRadius = 10;
  
  public static int getSelected(int x, int y, Graph graph, 
          int center, int width, int axis, boolean isThumb) {
      int nodeRadius = (isThumb)? thumbNodeRadius : largeNodeRadius;
      IAtomContainer atomContainer = graph.getAtomContainer(); 
      int numberOfNodes = atomContainer.getAtomCount();
      int separation = width / numberOfNodes;
      Map<IAtom, Integer> positions = getPositions(
              nodeRadius, atomContainer, separation, center, width, axis);
      int best = -1;
      double bestDist = Double.MAX_VALUE;
      for (int i = 0; i < numberOfNodes; i++) {
          IAtom atom = atomContainer.getAtom(i);
          int xPos = positions.get(atom);
          double dx = xPos - x;
          double dy = axis - y;
          double dist = (dx * dx) + (dy * dy);
          if (dist < bestDist) {
              bestDist = dist;
              best = i;
//              System.out.println(
//                      "dx " + dx + " dy " + dy + " dist " + dist + " NEW BEST " + i);
          } else {
//              System.out.println(
//                      "dx " + dx + " dy " + dy + " dist " + dist);
          }
      }
      return best;
  }
  
  public static Map<IAtom, Integer> getPositions(
          int nodeRadius, IAtomContainer atomContainer, 
          int separation, int center, int width, int axis) {
      int leftEdge = center - (width / 2); 
      int i = leftEdge + (separation / 2);
      Map<IAtom, Integer> nodePositions = new HashMap<IAtom, Integer>();
      for (IAtom atom : atomContainer.atoms()) {
          nodePositions.put(atom, i);
          i += separation;
      }
      return nodePositions;
  }
  
  public static void paintDiagram(
            Graph graph, Graphics g, 
            int center, int width, int axis, 
            boolean isThumb,
            int selectedAtom, int selectedBond) {
        int nodeRadius = (isThumb)? thumbNodeRadius : largeNodeRadius;
        
        IAtomContainer atomContainer = graph.getAtomContainer();
        int numberOfNodes = atomContainer.getAtomCount();
        int separation = width / numberOfNodes;
        
        HashMap<IAtom, Integer> nodePositions = new HashMap<IAtom, Integer>();
        int leftEdge = center - (width / 2); 
        int i = leftEdge + (separation / 2);

//        int height = width / 2;
//        paintBackground(g, leftEdge, axis, width, height);
       
        int d = nodeRadius * 2;
        for (IAtom atom : atomContainer.atoms()) {
            nodePositions.put(atom, i);
            g.fillOval(i - nodeRadius, axis - nodeRadius, d, d);
            i += separation;
        }
        
        int bondIndex = 0;
        for (IBond bond : atomContainer.bonds()) {
            Color color = Color.BLACK;
            int atom0 = atomContainer.getAtomNumber(bond.getAtom(0));
            int atom1 = atomContainer.getAtomNumber(bond.getAtom(1)); 
            if (atom0 == selectedAtom || atom1 == selectedAtom) {
                color = Color.RED;
            }
            boolean isSelected = bondIndex == selectedBond;
            paintBond(g, bond, color, atomContainer, 
                    nodeRadius, nodePositions, axis, isSelected);
            bondIndex++;
        }
        
        if (!isThumb) {
            g.setColor(Color.WHITE);
            for (IAtom atom : atomContainer.atoms()) {
                paintText(g, atom.getSymbol(), nodePositions.get(atom), axis);
            }
            g.setColor(Color.BLACK);
            int count = 0;
            int numberTrackAxis = axis + 20;
            int targetTrackAxis = axis + 40;
            List<Integer> atomTargetMap = graph.getAtomTargetMap();
            for (IAtom atom : atomContainer.atoms()) {
                int xPos = nodePositions.get(atom);
                if (selectedAtom != -1 && selectedAtom == count) {
                    paintSelectionElement(g, xPos, axis, nodeRadius * 2);
                }
                paintText(g, String.valueOf(count), xPos, numberTrackAxis);
                if (count < atomTargetMap.size()) {
                    int target = atomTargetMap.get(count);
                    paintText(g, String.valueOf(target), xPos, targetTrackAxis);
                }
                count++;
            }
        }
        
    }
    
    public static void paintText(Graphics g, String text, int cX, int cY) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(text, g);
        double w = bounds.getWidth();
        double h = bounds.getHeight();
        int baseX = (int) (cX - (w / 2));
        int baseY = (int) (cY + (fm.getAscent() - (h / 2)));
        g.drawString(text, baseX, baseY);
    }
    
    public static void paintSelectionElement(
            Graphics g, int xPos, int yPos, int d) {
        int r = d / 2;
        Color savedColor = g.getColor(); 
        g.setColor(Color.RED);
        g.drawRect(xPos - r, yPos - r, d, d);
        g.setColor(savedColor);
    }
    
    public static void paintBackground(
            Graphics g, int leftEdge, int axis, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillRect(leftEdge, axis - (height / 2), width, height);
        g.setColor(Color.BLACK);
    }
    
    public static void paintBond(
            Graphics g, IBond bond, Color color, IAtomContainer atomContainer,
            int nodeRadius,
            Map<IAtom, Integer> nodePositions, 
            int axis, boolean isSelected) {
        IAtom atomA = bond.getAtom(0);
        IAtom atomB = bond.getAtom(1);
        int posA = atomContainer.getAtomNumber(atomA);
        int posB = atomContainer.getAtomNumber(atomB);
        int leftEnd, rightEnd;
        if (posA < posB) {
            leftEnd = nodePositions.get(atomA);
            rightEnd = nodePositions.get(atomB);
        } else {
            leftEnd = nodePositions.get(atomB);
            rightEnd = nodePositions.get(atomA);
        }
        int arcWidth = rightEnd - leftEnd;
        int arcHeight = arcWidth / 2;
        int y = axis - (arcHeight / 2) - nodeRadius;
//        if (arc.isNew) {  // TODO
//            g.setColor(Color.RED);
//        }
        Color savedColor = g.getColor();
        if (isSelected) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(color);
        }
        g.drawArc(leftEnd, y, arcWidth, arcHeight, 180, -180);
        g.setColor(savedColor);
    }
}
