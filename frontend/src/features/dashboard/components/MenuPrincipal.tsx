import Link from "next/link";

const BASE_JSP = process.env.NEXT_PUBLIC_API_URL?.replace("/nextlog", "") ?? "http://localhost:8080";

const MENU_ITEMS = [
  {
    label: "Fretes",
    icon: "bi-truck-front-fill",
    href: `${BASE_JSP}/nextlog/fretes`,
    external: true,
    desc: "Gestão completa de fretes",
  },
  {
    label: "Clientes",
    icon: "bi-people-fill",
    href: `${BASE_JSP}/nextlog/clientes`,
    external: true,
    desc: "Cadastro e consulta de clientes",
  },
  {
    label: "Motoristas",
    icon: "bi-person-vcard-fill",
    href: `${BASE_JSP}/nextlog/motoristas`,
    external: true,
    desc: "Gestão de motoristas",
  },
  {
    label: "Veículos",
    icon: "bi-car-front-fill",
    href: `${BASE_JSP}/nextlog/veiculos`,
    external: true,
    desc: "Controle da frota de veículos",
  },
  {
    label: "Performance",
    icon: "bi-graph-up-arrow",
    href: "/dashboard/performance",
    external: false,
    desc: "Gráficos e indicadores operacionais",
  },
  {
    label: "Manutenções",
    icon: "bi-tools",
    href: "/dashboard/manutencao",
    external: false,
    desc: "Controle preventivo e corretivo",
  },
];

export function MenuPrincipal() {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
      {MENU_ITEMS.map(({ label, icon, href, external, desc }) => {
        const cls =
          "group flex flex-col gap-4 bg-white border border-gray-200 rounded-2xl p-6 hover:border-blue-400 hover:shadow-lg transition-all duration-200 cursor-pointer";
        const inner = (
          <>
            <div className="w-12 h-12 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600 group-hover:bg-blue-600 group-hover:text-white transition-all duration-200">
              <i className={`bi ${icon} text-xl`} />
            </div>
            <div>
              <p className="font-bold text-gray-900 mb-1">{label}</p>
              <p className="text-xs text-gray-500">{desc}</p>
            </div>
            <div className="flex items-center gap-1 text-xs text-blue-600 font-semibold mt-auto">
              {external ? (
                <>
                  <i className="bi bi-box-arrow-up-right text-xs" />
                  Acessar sistema
                </>
              ) : (
                <>
                  <i className="bi bi-arrow-right text-xs" />
                  Ver módulo
                </>
              )}
            </div>
          </>
        );
        return external ? (
          <a key={label} href={href} target="_blank" rel="noopener noreferrer" className={cls}>
            {inner}
          </a>
        ) : (
          <Link key={label} href={href} className={cls}>
            {inner}
          </Link>
        );
      })}
    </div>
  );
}