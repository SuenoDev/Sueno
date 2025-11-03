attribute vec4 aPos4D; // 4D position (x, y, z, w)

// Матрицы для 4D преобразований
uniform vec4 u_ModelView4D_col0;
uniform vec4 u_ModelView4D_col1;
uniform vec4 u_ModelView4D_col2;
uniform vec4 u_ModelView4D_col3;

uniform vec4 u_Projection4Dto3D_col0;
uniform vec4 u_Projection4Dto3D_col1;
uniform vec4 u_Projection4Dto3D_col2;
uniform vec4 u_Projection4Dto3D_col3;

// Матрицы для 3D преобразований
uniform vec4 u_View3D_col0;
uniform vec4 u_View3D_col1;
uniform vec4 u_View3D_col2;
uniform vec4 u_View3D_col3;

uniform vec4 u_Projection3D_col0;
uniform vec4 u_Projection3D_col1;
uniform vec4 u_Projection3D_col2;
uniform vec4 u_Projection3D_col3;

// НОВАЯ UNIFORM: 3D смещение объекта (в мировых 3D-координатах)
uniform vec3 u_ObjectOffset3D;

void main()
{
    // Реконструкция 4x4 матриц (без изменений)
    mat4 u_ModelView4D_reconstructed = mat4(
    u_ModelView4D_col0, u_ModelView4D_col1, u_ModelView4D_col2, u_ModelView4D_col3
    );
    mat4 u_Projection4Dto3D_reconstructed = mat4(
    u_Projection4Dto3D_col0, u_Projection4Dto3D_col1, u_Projection4Dto3D_col2, u_Projection4Dto3D_col3
    );
    mat4 u_View3D_reconstructed = mat4(
    u_View3D_col0, u_View3D_col1, u_View3D_col2, u_View3D_col3
    );
    mat4 u_Projection3D_reconstructed = mat4(
    u_Projection3D_col0, u_Projection3D_col1, u_Projection3D_col2, u_Projection3D_col3
    );

    // Шаг 1: Применяем 4D-трансформации к нашей 4D-позиции.
    vec4 transformedPos4D = u_ModelView4D_reconstructed * aPos4D;

    // Шаг 2: Проецируем 4D-координаты в 3D-пространство (W-перспектива).
    float distanceToWPlane = 3.5; // ПОДСТРОЙТЕ ЭТО ЗНАЧЕНИЕ!
    float wFactor = 1.0 / (distanceToWPlane - transformedPos4D.w);
    vec3 projected3DPos = transformedPos4D.xyz * wFactor;

    // НОВЫЙ ШАГ: Применяем 3D-смещение к спроецированной позиции
    projected3DPos += u_ObjectOffset3D;
    projected3DPos *= vec3(15.);

    // Шаг 3: Применяем 3D-видовую и 3D-проекционную матрицы.
    vec4 viewSpacePos = u_View3D_reconstructed * vec4(projected3DPos, 1.0);
    gl_Position = u_Projection3D_reconstructed * viewSpacePos;
}