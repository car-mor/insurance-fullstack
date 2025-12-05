import { useEffect, useState } from "react";

function App() {
  const [policies, setPolicies] = useState([]);
  
  // Estado para el formulario
  const [formData, setFormData] = useState({
    policyNumber: "",
    holderName: "",
    premiumAmount: "",
    startDate: "",
    status: "ACTIVE" // Valor por defecto
  });

  // Estado para guardar los errores de validación que vengan del servidor
  const [errors, setErrors] = useState({});

  const fetchPolicies = () => {
    fetch("http://localhost:8081/api/policies")
      .then((response) => response.json())
      .then((data) => setPolicies(data))
      .catch((error) => console.error("Error:", error));
  };

  // 2. LUEGO la usas en el useEffect
  useEffect(() => {
    fetchPolicies();
  }, []);

  // Cargar pólizas al inicio
  useEffect(() => {
    fetchPolicies();
  }, []);

  // Manejar cambios en los inputs del formulario
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Enviar el formulario (POST)
  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Limpiamos errores previos
    setErrors({});

    fetch("http://localhost:8081/api/policies", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then(async (response) => {
        if (response.ok) {
          return response.json();
        } else {
          // Si falla (ej. error 400), leemos el JSON de errores
          const errorData = await response.json();
          throw errorData; 
        }
      })
      .then((newPolicy) => {
        // ÉXITO: Agregamos la nueva póliza a la lista visualmente
        setPolicies([...policies, newPolicy]);
        // Limpiamos el formulario
        setFormData({
          policyNumber: "",
          holderName: "",
          premiumAmount: "",
          startDate: "",
          status: "ACTIVE"
        });
        alert("¡Póliza creada con éxito!");
      })
      .catch((errorMap) => {
        // ERROR: Guardamos los errores para mostrarlos en rojo
        console.error("Errores de validación:", errorMap);
        setErrors(errorMap);
      });
  };

  return (
    <div className="container mt-5">
      <h1 className="text-center text-primary mb-4">OpenPolicy Dashboard</h1>

      {/* --- FORMULARIO DE CREACIÓN --- */}
      <div className="card shadow mb-4">
        <div className="card-header bg-primary text-white">Nueva Póliza</div>
        <div className="card-body">
          <form onSubmit={handleSubmit} className="row g-3">
            
            <div className="col-md-3">
              <label className="form-label">Número de Póliza</label>
              <input 
                type="text" 
                name="policyNumber"
                className={`form-control ${errors.policyNumber ? 'is-invalid' : ''}`} 
                placeholder="Ej. POL-1234"
                value={formData.policyNumber}
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.policyNumber}</div>
            </div>

            <div className="col-md-3">
              <label className="form-label">Titular</label>
              <input 
                type="text" 
                name="holderName"
                className={`form-control ${errors.holderName ? 'is-invalid' : ''}`}
                placeholder="Nombre completo"
                value={formData.holderName}
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.holderName}</div>
            </div>

            <div className="col-md-2">
              <label className="form-label">Prima ($)</label>
              <input 
                type="number" 
                name="premiumAmount"
                className={`form-control ${errors.premiumAmount ? 'is-invalid' : ''}`}
                placeholder="0.00"
                value={formData.premiumAmount}
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.premiumAmount}</div>
            </div>

            <div className="col-md-2">
              <label className="form-label">Fecha Inicio</label>
              <input 
                type="date" 
                name="startDate"
                className={`form-control ${errors.startDate ? 'is-invalid' : ''}`}
                value={formData.startDate}
                onChange={handleChange}
              />
              <div className="invalid-feedback">{errors.startDate}</div>
            </div>

            <div className="col-md-2 d-flex align-items-end">
              <button type="submit" className="btn btn-success w-100">Guardar</button>
            </div>

          </form>
        </div>
      </div>

      {/* --- TABLA DE DATOS --- */}
      <div className="card shadow">
        <div className="card-header bg-dark text-white">
          Pólizas Registradas
        </div>
        <div className="card-body">
          <table className="table table-hover table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>Número</th>
                <th>Titular</th>
                <th>Prima ($)</th>
                <th>Inicio</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {policies.map((policy) => (
                <tr key={policy.id}>
                  <td>{policy.id}</td>
                  <td>{policy.policyNumber}</td>
                  <td>{policy.holderName}</td>
                  <td>{policy.premiumAmount}</td>
                  <td>{policy.startDate}</td>
                  <td>
                    <span className={`badge ${policy.status === 'ACTIVE' ? 'text-bg-success' : 'text-bg-warning'}`}>
                      {policy.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default App;