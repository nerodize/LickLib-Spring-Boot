export function HomePage({ onStart }: { onStart: () => void }) {
  return (
    <div className="flex flex-col items-center justify-center min-h-[80vh] text-center px-4">
      {/* Background Glow Effekt */}
      <div className="absolute top-1/4 left-1/2 -translate-x-1/2 w-64 h-64 bg-purple-600/20 blur-[120px] -z-10" />
      
      <h1 className="text-6xl font-black tracking-tighter mb-6 bg-gradient-to-b from-white to-slate-500 bg-clip-text text-transparent leading-tight">
        Master Your Guitar <br /> 
        <span className="text-purple-500">One Lick At A Time.</span>
      </h1>
      
      <p className="text-slate-400 text-xl max-w-2xl mb-10 leading-relaxed">
        LickLib ist dein persönliches Archiv für Riffs, Licks und Inspiration. 
        Mappe deine Kreativität direkt in deine Datenbank.
      </p>

      <div className="flex space-x-4">
        <button 
          onClick={onStart}
          className="bg-purple-600 hover:bg-purple-500 text-white px-8 py-4 rounded-2xl font-bold text-lg transition-all shadow-[0_0_20px_rgba(147,51,234,0.3)] hover:scale-105"
        >
          Get Started
        </button>
        <button className="bg-slate-800 hover:bg-slate-700 text-slate-200 px-8 py-4 rounded-2xl font-bold text-lg transition-all">
          View Community
        </button>
      </div>

      {/* Kleine Feature-Cards */}
      <div className="grid grid-cols-3 gap-8 mt-24 w-full max-w-4xl">
        {[
          { title: "Fast Mapping", desc: "Spring Boot & React Integration" },
          { title: "Entity Based", desc: "Pure TypeScript Interfaces" },
          { title: "Dark First", desc: "Designed for developers" }
        ].map((f, i) => (
          <div key={i} className="bg-slate-900/50 border border-slate-800 p-6 rounded-2xl text-left">
            <h3 className="font-bold text-purple-400 mb-2">{f.title}</h3>
            <p className="text-sm text-slate-500">{f.desc}</p>
          </div>
        ))}
      </div>
    </div>
  );
}