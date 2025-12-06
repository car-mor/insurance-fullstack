import { useEffect, useState, useCallback } from "react";

function App() {
  // --- ESTADOS ---
  const [token, setToken] = useState(localStorage.getItem("jwt_token") || ""); 
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("jwt_token"));
  
  const [credentials, setCredentials] = useState({ username: "", password: "" });
  const [loginError, setLoginError] = useState(false);

  const [policies, setPolicies] = useState([]);
  const [formData, setFormData] = useState({
    policyNumber: "", holderName: "", premiumAmount: "", startDate: ""
  });
  const [formErrors, setFormErrors] = useState({});

  // 1. DEFINIR LOGOUT PRIMERO (Para que otros puedan usarla)
  const logout = useCallback(() => {
    setToken("");
    localStorage.removeItem("jwt_token");
    setIsLoggedIn(false);
    setPolicies([]);
  }, []);

  // 2. HEADER HELPER
  const getAuthHeader = () => {
    return `Bearer ${token}`; 
  };

  // 3. CARGAR DATOS (Ahora sí 'logout' ya existe arriba)
  const fetchPolicies = useCallback(() => {
    fetch("http://localhost:8081/api/policies", {
      headers: { "Authorization": `Bearer ${token}` }
    })
      .then((res) => {
        // Si el token expiró (403) o es inválido (401), cerramos sesión
        if (res.status === 403 || res.status === 401) {
            logout(); 
            return;
        }
        return res.json();
      })
      .then((data) => {
          if(data && Array.isArray(data)) setPolicies(data);
      })
      .catch(err => console.error(err));
  }, [token, logout]); 

  // 4. EFECTO DE CARGA AUTOMÁTICA
  useEffect(() => {
    if (isLoggedIn && token) {
      fetchPolicies();
    }
  }, [isLoggedIn, token, fetchPolicies]);

  // 5. LOGIN
  const handleLogin = (e) => {
    e.preventDefault();
    setLoginError(false);
    
    fetch("http://localhost:8081/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials)
    })
    .then(response => {
      if (response.ok) return response.json();
      throw new Error("Error de autenticación");
    })
    .then(data => {
      const receivedToken = data.token;
      setToken(receivedToken);
      localStorage.setItem("jwt_token", receivedToken);
      setIsLoggedIn(true);
    })
    .catch(() => setLoginError(true));
  };

  // 6. CREAR PÓLIZA
  const handleCreate = (e) => {
    e.preventDefault();
    setFormErrors({});

    fetch("http://localhost:8081/api/policies", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": getAuthHeader()
      },
      body: JSON.stringify(formData),
    })
      .then(async (response) => {
        if (response.ok) return response.json();
        const errorData = await response.json();
        throw errorData; 
      })
      .then(() => {
        fetchPolicies(); 
        alert("¡Creada con éxito!");
        setFormData({ policyNumber: "", holderName: "", premiumAmount: "", startDate: "" });
      })
      .catch((err) => {
        console.error(err);
        if(err.policyNumber || err.holderName) {
            setFormErrors(err);
        } else {
            alert("Error al guardar: " + (err.message || "Desconocido"));
        }
      });
  };

  const handleLoginChange = (e) => setCredentials({...credentials, [e.target.name]: e.target.value});
  const handleFormChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  // --- VISTAS ---
  if (!isLoggedIn) {
    return (
      <div className="container mt-5 d-flex justify-content-center">
        <div className="card shadow p-4" style={{ width: "400px" }}>
          <h2 className="text-center text-primary mb-4">Iniciar Sesión (JWT)</h2>
          <form onSubmit={handleLogin}>
            <div className="mb-3">
              <label>Usuario</label>
              <input type="text" name="username" className="form-control" onChange={handleLoginChange} placeholder="admin"/>
            </div>
            <div className="mb-3">
              <label>Contraseña</label>
              <input type="password" name="password" className="form-control" onChange={handleLoginChange} placeholder="admin123"/>
            </div>
            {loginError && <div className="alert alert-danger">Usuario o contraseña incorrectos</div>}
            <button className="btn btn-primary w-100">Obtener Token</button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 className="text-primary">OpenPolicy Dashboard</h1>
            <span className="badge bg-secondary">Modo Seguro: JWT Activado</span>
        </div>
        <button onClick={logout} className="btn btn-outline-danger">Cerrar Sesión</button>
      </div>

      <div className="card shadow mb-4">
        <div className="card-header bg-primary text-white">Nueva Póliza</div>
        <div className="card-body">
          <form onSubmit={handleCreate} className="row g-3">
             <div className="col-md-3">
               <input type="text" name="policyNumber" className="form-control" placeholder="POL-XXXX" onChange={handleFormChange} value={formData.policyNumber}/>
               <div className="text-danger small">{formErrors.policyNumber}</div>
             </div>
             <div className="col-md-3">
               <input type="text" name="holderName" className="form-control" placeholder="Titular" onChange={handleFormChange} value={formData.holderName}/>
               <div className="text-danger small">{formErrors.holderName}</div>
             </div>
             <div className="col-md-2">
               <input type="number" name="premiumAmount" className="form-control" placeholder="Prima" onChange={handleFormChange} value={formData.premiumAmount}/>
               <div className="text-danger small">{formErrors.premiumAmount}</div>
             </div>
             <div className="col-md-2">
               <input type="date" name="startDate" className="form-control" onChange={handleFormChange} value={formData.startDate}/>
               <div className="text-danger small">{formErrors.startDate}</div>
             </div>
             <div className="col-md-2">
               <button className="btn btn-success w-100">Guardar</button>
             </div>
          </form>
        </div>
      </div>

      <table className="table table-striped shadow-sm">
        <thead className="table-dark">
          <tr><th>ID</th><th>Número</th><th>Titular</th><th>Prima</th><th>Estado</th></tr>
        </thead>
        <tbody>
          {policies.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td><td>{p.policyNumber}</td><td>{p.holderName}</td><td>{p.premiumAmount}</td>
              <td><span className="badge text-bg-success">{p.status}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;