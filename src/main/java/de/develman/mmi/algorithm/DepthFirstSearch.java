package de.develman.mmi.algorithm;

import de.develman.mmi.model.Graph;
import de.develman.mmi.model.Vertex;
import de.develman.mmi.model.VisitingState;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse DepthFirstSearch implementiert die Tiefensuche in einem Graphen.
 *
 * @author Georg Henkel <georg@develman.de>
 */
public class DepthFirstSearch
{
    private static boolean vertexFound = false;

    /**
     * Tiefensuche vom Startknoten, die alle besuchten Knoten liefert
     *
     * @param startVertex Startknoten
     * @return Liste der Knoten, die vom Startknoten erreicht werden
     */
    public static List<Vertex> getAccessibleVertices(Vertex startVertex)
    {
        vertexFound = false;

        List<Vertex> visitList = new ArrayList<>();
        doSearchInternal(visitList, startVertex, null);

        return visitList;
    }

    /**
     * Prüft, ob es einen Weg zwischen Start- und Endknoten gibt
     *
     * @param startVertex Startknoten
     * @param endVertex Endknoten
     * @return {@code true}, wenn ein Weg gefunden wurde
     */
    public static boolean hasPath(Vertex startVertex, Vertex endVertex)
    {
        vertexFound = false;
        doSearchInternal(new ArrayList<>(), startVertex, endVertex);

        return vertexFound;
    }

    /**
     * Breitensuche von Startknoten zu Endknoten
     *
     * @param startVertex Startknoten
     * @param endVertex Endknoten
     * @return Liste der besuchten Knoten, von Startknoten bis Endknoten
     */
    public static List<Vertex> getVerticesOnPath(Vertex startVertex, Vertex endVertex)
    {
        vertexFound = false;
        List<Vertex> visitList = new ArrayList<>();
        doSearchInternal(visitList, startVertex, endVertex);

        return visitList;
    }

    /**
     * Liefert die Anzahl an Zusammenhangskomponenten
     *
     * @param graph Graph
     * @param startVertex Startknoten
     * @return Liste der Knoten, die vom Startknoten erreicht werden
     */
    public static int countComponents(Graph graph, Vertex startVertex)
    {
        int countComponents = 0;
        List<Vertex> vertices = new ArrayList<>(graph.getVertices());

        boolean allFound = false;
        do
        {
            countComponents++;

            startVertex = findComponent(vertices, startVertex);
            if (startVertex == null)
            {
                allFound = true;
            }
        }
        while (!allFound);

        return countComponents;
    }

    private static void doSearchInternal(List<Vertex> visitList, Vertex startVertex, Vertex endVertex)
    {
        if (vertexFound)
        {
            return;
        }

        visitList.add(startVertex);
        if (startVertex.equals(endVertex))
        {
            vertexFound = true;
            return;
        }

        startVertex.setVisitingState(VisitingState.VISITED);
        startVertex.getSuccessors().stream().filter(vertex -> vertex.getVisitingState() == VisitingState.NOT_VISITED).forEach(
                vertex -> doSearchInternal(visitList, vertex, endVertex));
    }

    private static Vertex findComponent(List<Vertex> vertices, Vertex startVertex)
    {
        Vertex nextStartVertex = null;

        List<Vertex> foundVertices = new ArrayList<>();
        doSearchInternal(foundVertices, startVertex, null);

        vertices.removeAll(foundVertices);
        if (!vertices.isEmpty())
        {
            nextStartVertex = vertices.get(0);
        }

        return nextStartVertex;
    }
}
