import java.util.ArrayList;

//classe che costruisce una sfera attraverso i parametri
//posizione (centro) e raggio
//La classe ha la funzione che calcola l'intersezione 
//della sfera con un raggio, e la funzione che restituisce
//la normale alla sfera in un punto dato
public class Sphere {
	// raggio
	public float rad; 
	// posizione (centro)
	public Point3D p;
	public ArrayList<Triangle> triangles = new ArrayList<>();
	public int matId;
	
	//costruttore
	public Sphere(float nrad, Point3D np, int matId) {
		rad=nrad;
		p=np;
		this.matId = matId;

		createTriangles(nrad, np, 50);
	}

  //metodo che imposta, a seconda della scelta
  //effettuata dall'utente, la posizione
  //appropriata alle prime tre sfere
  //Prende come parametro l'indice dell'array
  //spheres di cui si deve settare la posizione
  static void setSpheresPosition() {
    //vettore costruttore delle sfere
    RenderAction.spheres.add(new Sphere(1, new Point3D(-4.0f,0.0f,0.0f), 1));
    RenderAction.spheres.add(new Sphere(1, new Point3D(-7.0f,0.0f,0.0f), 1));
    RenderAction.spheres.add(new Sphere(1, new Point3D(-4.0f,0.0f,5.3f), 1));
  }

  //funzione di intersezione con un raggio: Return
	//distanza o -1.0f se non c'e' intersezione
	double intersect(Ray r){
		//sostituendo il raggio o+td all'equazione della 
		//sfera (td+(o-p)).(td+(o-p)) -R^2 =0 si ottiene 
		//l'intersezione. Si deve risolvere
		//t^2*d.d + 2*t*(o-p).d + (o-p).(o-p)-R^2 = 0
		// A=d.d=1 (r.d e' normalizzato)
		// B=2*(o-p).d
		// C=(o-p).(o-p)-R^2
		Point3D op = p.subtract(r.o);
		// calcolo della distanza
		double t;
		// 2*t*B -> semplificato usando b/2 invece di B
		double B=op.dotProduct(r.d);
		double C=op.dotProduct(op)-rad*rad;
	    //determinante equazione quadratica
		double det=B*B-C;
	    // se e' negativo non c'e' intersezione reale, 
		//altrimenti assegna a det il risultato
		if (det<0) 
			return -1.0f; 
		else { 
			det=(float) Math.sqrt(det);
	        //ritorna la t piu' piccola se questa e' >0 
			//altrimenti vedi quella piu' grande se e' >0 
			//se sono tutte negative non c'e' intersezione
			if((t=B-det)> Utilities.EPSILON)
				return t;
			else {
				if((t=B+det)> Utilities.EPSILON)
					return t;
				else
					return -1.0f;
			}
		}
			
	}

	//funzione che calcola la normale in un punto iP della 
	//sfera
	Point3D normal(Point3D iP) {
		//vettore dal centro all'intersezione normalizzato
		return (iP.subtract(p)).getNormalizedPoint();
	}

	public void createTriangles(double r, Point3D c, int n) {
    /* createTriangles() genra una mesh sferica di triangonli
    la sfera ha raggio r, centro c ed e’ divisa in n
    paralleli ed
    n meridiani. In tutto e’ composta da n^2 triangoli.
    La sfera ha colore (R,G,B) */

		double x, y, z;
		double pi = Math.PI;
		int i, j;
		ArrayList<Point3D> tpts = new ArrayList<>();
		Triangle t;
		Point3D p = null;
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				x = r * Math.sin(i * pi / n) * Math.cos(2 * j * pi / n) + c.x;
				y = r * Math.sin(i * pi / n) * Math.sin(2 * j * pi / n) + c.y;
				z = -r * Math.cos(i * pi / n) + c.z;
				p = new Point3D(x, y, z);
				tpts.add(p);
			}
		}

		for (i = 0; i < n - 1; i++) {
			for (j = 0; j < n; j++) {
				t = new Triangle(tpts.get((i * n + j) % (n * n)),
						tpts.get(((i + 1) * n + j) % (n * n)),
						tpts.get((i * n + j + 1) % (n * n)));

				t.n = p.subtract(c);
				t.matId = this.matId;
				triangles.add(t);

				t = new Triangle(tpts.get(((i + 1) * n + j) % (n * n)),
						tpts.get(((i + 1) * n + j + 1) % (n * n)),
						tpts.get((i * n + j + 1) % (n * n)));

				t.n = p.subtract(c);
				t.matId = this.matId;
				triangles.add(t);
			}
		}
	}
}
