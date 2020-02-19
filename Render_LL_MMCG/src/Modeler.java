import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

class Modeler extends JDialog {
  private final double SIZE = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2;
  private int radius = 75;

  class ImagePanel extends JPanel {
    ArrayList<Ellipse2D> ellipse2DS = new ArrayList<>();

    ImagePanel() {
      super(null);

      repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(1.0f));

      if (RenderAction.spheres != null) {
        for (Sphere s : RenderAction.spheres) {
          g2.draw(new Ellipse2D.Double(SIZE + (s.p.x*50) , s.p.z*50 + radius, s.rad*radius, s.rad*radius));
        }
      }

      if (ellipse2DS != null) {
        for (Ellipse2D e : ellipse2DS) {
          g2.draw(e);
        }
      }
    }
  }

  private JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
  private JButton deleteButton = new JButton("Elimina ultima sfera");
  private JButton doneButton = new JButton("Fatto");

  ImagePanel imagePanel = new ImagePanel();

  Modeler(JDialog frame) {
    super(frame, "Modellatore", true);

    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!RenderAction.additionalSpheres.isEmpty() && !imagePanel.ellipse2DS.isEmpty()) {
          RenderAction.additionalSpheres.remove(RenderAction.additionalSpheres.size()-1);
          imagePanel.ellipse2DS.remove(imagePanel.ellipse2DS.size()-1);

          imagePanel.repaint();
        }
      }
    });

    doneButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (e.getButton() == MouseEvent.BUTTON1) {
          //TODO add slider to manage sphere radius
          //TODO fix -8 & -31 parameters (incorrect position from MouseEvent e)
          imagePanel.ellipse2DS.add(new Ellipse2D.Double(e.getX() - (radius/ (double) 2) -8, e.getY()- (radius/ (double) 2) -31, radius, radius));
          Point3D newPoint = new Point3D(((e.getX() - SIZE)/ (double) 40), 0, e.getY()/ (double) 75);
          RenderAction.additionalSpheres.add(new Sphere(1, newPoint));

          repaint();
        }
      }
    });

    buttonsPanel.add(deleteButton);
    buttonsPanel.add(doneButton);

    add(buttonsPanel, BorderLayout.PAGE_END);

    add(imagePanel);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);

        int choice = JOptionPane.showConfirmDialog(Modeler.this, "Vuoi salvare le sfere create?", "Conferma", JOptionPane.YES_NO_OPTION);

        switch (choice) {
          case JOptionPane.YES_OPTION:
            dispose();
            break;
          case JOptionPane.NO_OPTION:
            RenderAction.additionalSpheres.clear();
            dispose();
            break;
        }
      }
    });

    setMinimumSize(new Dimension((int) SIZE, (int) SIZE));
    setLocationRelativeTo(frame);
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setVisible(true);
  }
}