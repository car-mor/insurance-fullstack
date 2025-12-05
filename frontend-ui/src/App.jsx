import { useEffect, useState } from "react";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [credentials, setCredentials] = useState({ username: "", password: "" });
  const [loginError, setLoginError] = useState(false);

  const [policies, setPolicies] = useState([]);
  const [formData, setFormData] = useState({
    policyNumber: "", holderName: "", premiumAmount: "", startDate: ""
  });
  const [formErrors, setFormErrors] = useState({});

//   btoa(...): No es encriptación real (se puede revertir fácil), pero es el estándar para que los caracteres especiales (:, /, @) no rompan la transmisión por internet.

// Authorization: Basic ...: Es la palabra clave que Spring Security busca. Si llega una petición sin esto, responde "401 Unauthorized" (No estás autorizado).

// Tomamos usuario y contraseña y los "envolvemos" en Base64 para que viajen seguros por el cable.
  // Generar Header de Autenticación
  const getAuthHeader = () => {
    return "Basic " + btoa(`${credentials.username}:${credentials.password}`);
  };

  // --- SOLUCIÓN ERROR LINT ---
  // Definimos fetchPolicies para poder reusarlo
  const fetchPolicies = () => {
    fetch("http://localhost:8081/api/policies", {
      // Muestra credenciales al pedir (Fetch)
      headers: { "Authorization": getAuthHeader() }
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error cargando datos");
        return res.json();
      })
      .then((data) => setPolicies(data))
      .catch(err => console.error(err));
  };

  // USAMOS useEffect PARA LLAMARLO AUTOMÁTICAMENTE CUANDO HAYA LOGIN
  // Esto elimina el error "fetchPolicies is assigned but never used"
  useEffect(() => {
    if (isLoggedIn) {
      fetchPolicies();
    }
  }, [isLoggedIn]); // Se ejecuta cada vez que 'isLoggedIn' cambia


  // Manejo de Login
  const handleLogin = (e) => {
    e.preventDefault();
    // Probamos credenciales haciendo un fetch simple
    fetch("http://localhost:8081/api/policies", {
      headers: { "Authorization": getAuthHeader() }
    })
    .then(response => {
      if (response.ok) {
        setIsLoggedIn(true);
        setLoginError(false);
        // Nota: Ya no necesitamos llamar fetchPolicies() aquí manualmente
        // porque el useEffect de arriba lo hará solito al cambiar isLoggedIn a true.
      } else {
        throw new Error("Credenciales inválidas");
      }
    })
    .catch(() => setLoginError(true));
  };

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
        // Recargamos la lista completa usando la función reutilizable
        fetchPolicies();
        alert("¡Creada con éxito!");
        setFormData({ policyNumber: "", holderName: "", premiumAmount: "", startDate: "" });
      })
      .catch((err) => {
        console.error(err);
        if(err.policyNumber || err.holderName) {
            setFormErrors(err);
        } else {
            alert("Error al guardar");
        }
      });
  };

  const handleLoginChange = (e) => setCredentials({...credentials, [e.target.name]: e.target.value});
  const handleFormChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  if (!isLoggedIn) {
    return (
      <div className="container mt-5 d-flex justify-content-center">
        <div className="card shadow p-4" style={{ width: "400px" }}>
          <h2 className="text-center text-primary mb-4">Iniciar Sesión</h2>
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
            <button className="btn btn-primary w-100">Entrar</button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="text-primary">OpenPolicy Dashboard</h1>
        <button onClick={() => setIsLoggedIn(false)} className="btn btn-outline-danger">Salir</button>
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