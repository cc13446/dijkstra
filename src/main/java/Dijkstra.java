import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Dijkstra {
    // 图的邻接矩阵表示
    static int[][] array;
    // 节点最短距离
    static int[] distance;
    // 节点最短路径的前置节点
    static int[] preNode;
    // 被访问过的节点集合
    static Set<Integer> visited;

    // 算法核心实现
    public static void dijkstra(int beginIndex) {
        System.arraycopy(array[beginIndex], 0, distance, 0, array.length);
        preNode[beginIndex] = beginIndex;
        visited.add(beginIndex);
        // 执行节点数-1次
        for (int i = 0; i < array.length - 1; i++) {
            // 寻找集合内节点最近邻居
            int index = beginIndex;
            int preIndex = beginIndex;
            int minDistance = Integer.MAX_VALUE;
            for (int node : visited) {
                for (int neighbour = 0; neighbour < array.length; neighbour++) {
                    if (!visited.contains(neighbour) && array[node][neighbour] < minDistance) {
                        minDistance = array[node][neighbour];
                        index = neighbour;
                        preIndex = node;
                    }
                }
            }
            if (index == beginIndex) {
                // 找不到最近邻居
                break;
            }
            visited.add(index);
            preNode[index] = preIndex;
            distance[index] = distance[preIndex] + array[preIndex][index];

            for (int j = 0; j < array.length; j++) {
                if (!visited.contains(j)
                        && array[index][j] != Integer.MAX_VALUE
                        &&distance[j] > distance[index] + array[index][j]) {
                    distance[j] = distance[index] + array[index][j];
                }
            }

            System.out.println("Distance：" + Arrays.toString(distance));
            System.out.println("PreNode：" + Arrays.toString(preNode));
            System.out.println("\n");
        }
    }

    // 初始化
    public static void initResources() {
        // 从文件读取图的邻接矩阵
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/map.txt"))) {
            int length = Integer.parseInt(reader.readLine().trim());
            if (length <= 0) {
                throw new RuntimeException("length of map must bigger than 0");
            }
            array = new int[length][length];
            for (int i = 0; i < length; i++) {
                String[] strings = reader.readLine().split(",");
                array[i] = Arrays.stream(strings).sequential().map(String::trim)
                        .mapToInt(Integer::parseInt).map(a -> {
                            if (a == -1) {
                                return Integer.MAX_VALUE;
                            } else {
                                return a;
                            }
                        }).toArray();
            }
            checkMapArray();
            distance = new int[length];
            for (int i = 0; i < length; i++) {
                distance[i] = Integer.MAX_VALUE;
            }
            preNode = new int[length];
            visited = new HashSet<>();
        } catch (IOException e) {
            throw new RuntimeException("map.txt not found " + e.getMessage());
        }

    }

    // 检查邻接矩阵的有效性
    private static void checkMapArray() {
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j < array[0].length; j++) {
                // 相同节点距离为0
                if (j == i && array[i][j] != 0) {
                    throw new RuntimeException("the distance of same node should be zero");
                }
                // 必须为对称矩阵
                if (array[i][j] != array[j][i]) {
                    throw new RuntimeException("the array of map should be symmetric matrix");
                }
            }
        }
    }

    public static void main(String[] args) {
        initResources();
        dijkstra(1);
    }

}
