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
	
	//costruttore di default
	public Sphere() {
		rad=0.0f;
		p=new Point3D();
	}
	
	//costruttore
	public Sphere(float nrad, Point3D np) {
		rad=nrad;
		p=np;
	}
		
	//funzione di intersezione con un raggio: Return 
	//distanza o -1.0f se non c'e' intersezione
	float intersect(Ray r){
		//sostituendo il raggio o+td all'equazione della 
		//sfera (td+(o-p)).(td+(o-p)) -R^2 =0 si ottiene 
		//l'intersezione. Si deve risolvere
		//t^2*d.d + 2*t*(o-p).d + (o-p).(o-p)-R^2 = 0
		// A=d.d=1 (r.d e' normalizzato)
		// B=2*(o-p).d
		// C=(o-p).(o-p)-R^2
		Point3D op = p.subtract(r.o);
		// calcolo della distanza
		float t;
		// 2*t*B -> semplificato usando b/2 invece di B
		float B=op.dotProduct(r.d);
		float C=op.dotProduct(op)-rad*rad;
	    //determinante equazione quadratica
		float det=B*B-C;
	    // se e' negativo non c'e' intersezione reale, 
		//altrimenti assegna a det il risultato
		if (det<0) 
			return -1.0f; 
		else { 
			det=(float) Math.sqrt(det);
	        //ritorna la t piu' piccola se questa e' >0 
			//altrimenti vedi quella piu' grande se e' >0 
			//se sono tutte negative non c'e' intersezione
			if((t=B-det)>Utilities.EPSILON)
				return t;
			else {
				if((t=B+det)>Utilities.EPSILON)
					return t;
				else
					return -1.0f;
			}
		}
			
	}

	//funzione che calcola la normale in un punto iP della 
	//sfera
    Point3D normal(Point3D iP){
        //vettore dal centro all'intersezione normalizzato
        return (iP.subtract(p)).getNormalizedPoint();
    }
    
    
	public static void main(String[] args) {
		
	}

}
