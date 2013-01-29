package edu.illinois.ncsa.versus.extract.impl;

import java.util.*;

import edu.illinois.ncsa.versus.*;
import edu.illinois.ncsa.versus.adapter.*;
import edu.illinois.ncsa.versus.adapter.impl.MeshAdapter;
import edu.illinois.ncsa.versus.descriptor.*;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.descriptor.impl.VectorFeature;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.utility.HasCategory;
import edu.ncsa.model.MeshAuxiliary.Face;
import edu.ncsa.model.MeshAuxiliary.Point;
import kgm.image.*;

/**
 * Extract a light field signature from an object that has a mesh.
 *
 * @author Kenton McHenry
 */
public class LightFieldExtractor implements Extractor, HasCategory {

    /**
     * Extract a double array representation of an object that has a mesh.
     *
     * @param adapter an object that has a mesh
     * @return a signature for the given object
     */
    private VectorFeature<double[]> extract(HasMesh adapter) {
        VectorFeature<double[]> signature = new VectorFeature<double[]>();
        Vector<Point> vertices = adapter.getMesh().getVertices();
        Vector<Face> faces = adapter.getMesh().getFaces();
        Point center = adapter.getMesh().getCenter();
        double radius = adapter.getMesh().getRadius();
        Vector<Point> PC = adapter.getMesh().getPC();

        int w = 200;
        int h = w;
        int halfw = (int) Math.floor(w / 2.0) - 1;
        int[] front = new int[w * h];
        int[] side = new int[w * h];
        int[] top = new int[w * h];
        double[] front_g, side_g, top_g;
        Pixel p0, p1, p2;
        Face face;

        //Transform the vertices so that they are aligned with XYZ axis and occupy the center of the target image.
        vertices = Point.transform(vertices, center, radius, PC);
        vertices = Point.transform(vertices, halfw, halfw);

        //Orthographically render lightfields from various sides
        for (int f = 0; f < faces.size(); f++) {
            face = faces.get(f);

            //Draw triangles orthographically from front
            if (face.v.length == 3) {
                p0 = new Pixel(vertices.get(face.v[0]).x, vertices.get(face.v[0]).y);
                p1 = new Pixel(vertices.get(face.v[1]).x, vertices.get(face.v[1]).y);
                p2 = new Pixel(vertices.get(face.v[2]).x, vertices.get(face.v[2]).y);

                ImageUtility.drawTriangle(front, w, h, p0, p1, p2, 0x00ffffff);
            } else {	//Assume polygons are simple
                for (int i = 1; i < face.v.length - 1; i++) {
                    p0 = new Pixel(vertices.get(face.v[0]).x, vertices.get(face.v[0]).y);
                    p1 = new Pixel(vertices.get(face.v[i]).x, vertices.get(face.v[i]).y);
                    p2 = new Pixel(vertices.get(face.v[i + 1]).x, vertices.get(face.v[i + 1]).y);

                    ImageUtility.drawTriangle(front, w, h, p0, p1, p2, 0x00ffffff);
                }
            }

            //Draw triangles orthographically from side
            if (face.v.length == 3) {
                p0 = new Pixel(vertices.get(face.v[0]).z, vertices.get(face.v[0]).y);
                p1 = new Pixel(vertices.get(face.v[1]).z, vertices.get(face.v[1]).y);
                p2 = new Pixel(vertices.get(face.v[2]).z, vertices.get(face.v[2]).y);

                ImageUtility.drawTriangle(side, w, h, p0, p1, p2, 0x00ffffff);
            } else {	//Assume polygons are simple
                for (int i = 1; i < face.v.length - 1; i++) {
                    p0 = new Pixel(vertices.get(face.v[0]).z, vertices.get(face.v[0]).y);
                    p1 = new Pixel(vertices.get(face.v[i]).z, vertices.get(face.v[i]).y);
                    p2 = new Pixel(vertices.get(face.v[i + 1]).z, vertices.get(face.v[i + 1]).y);

                    ImageUtility.drawTriangle(side, w, h, p0, p1, p2, 0x00ffffff);
                }
            }

            //Draw triangles orthographically from top
            if (face.v.length == 3) {
                p0 = new Pixel(vertices.get(face.v[0]).x, vertices.get(face.v[0]).z);
                p1 = new Pixel(vertices.get(face.v[1]).x, vertices.get(face.v[1]).z);
                p2 = new Pixel(vertices.get(face.v[2]).x, vertices.get(face.v[2]).z);

                ImageUtility.drawTriangle(top, w, h, p0, p1, p2, 0x00ffffff);
            } else {	//Assume polygons are simple
                for (int i = 1; i < face.v.length - 1; i++) {
                    p0 = new Pixel(vertices.get(face.v[0]).x, vertices.get(face.v[0]).z);
                    p1 = new Pixel(vertices.get(face.v[i]).x, vertices.get(face.v[i]).z);
                    p2 = new Pixel(vertices.get(face.v[i + 1]).x, vertices.get(face.v[i + 1]).z);

                    ImageUtility.drawTriangle(top, w, h, p0, p1, p2, 0x00ffffff);
                }
            }
        }

        //Set the signature
        front_g = ImageUtility.argb2g(front, w, h);
        side_g = ImageUtility.argb2g(side, w, h);
        top_g = ImageUtility.argb2g(top, w, h);

        front_g = ImageUtility.g2bw(front_g, w, h, 0.5);
        side_g = ImageUtility.g2bw(side_g, w, h, 0.5);
        top_g = ImageUtility.g2bw(top_g, w, h, 0.5);

        signature.add(0, front_g);
        signature.add(1, side_g);
        signature.add(2, top_g);

        //View the lightfields
        if (false) {
            ImageViewer viewer = new ImageViewer();

            viewer.add(front_g, w, h, false);
            viewer.add(side_g, w, h, false);
            viewer.add(top_g, w, h, false);
        }

        return signature;
    }

    @Override
    public MeshAdapter newAdapter() {
        return null;
    }

    @Override
    public String getName() {
        return "Light Fields";
    }

    @Override
    public Descriptor extract(Adapter adapter) throws UnsupportedTypeException {
        if (adapter instanceof HasMesh) {
            return extract((HasMesh) adapter);
        } else {
            throw new UnsupportedTypeException();
        }
    }

    @Override
    public Set<Class<? extends Adapter>> supportedAdapters() {
        Set<Class<? extends Adapter>> adapters = new HashSet<Class<? extends Adapter>>();

        adapters.add(HasFaces.class);

        return adapters;
    }

    @Override
    public Class<? extends Descriptor> getFeatureType() {
        return DoubleArrayFeature.class;
    }

    @Override
    public boolean hasPreview() {
        return false;
    }

    @Override
    public String previewName() {
        return null;
    }

    @Override
    public String getCategory() {
        return "3D";
    }
}