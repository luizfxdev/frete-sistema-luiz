"use client";

interface Props {
  onClose: () => void;
}

export function ConfirmacaoModal({ onClose }: Props) {
  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-6">
      <div className="bg-white rounded-3xl max-w-lg w-full p-10 text-center shadow-2xl animate-fade-in-up">
        <div className="w-16 h-16 bg-success/10 rounded-full flex items-center justify-center mx-auto mb-6">
          <i className="bi bi-check-circle-fill text-success text-3xl" />
        </div>
        <h2 className="text-2xl font-bold text-brand-900 mb-4">Candidatura enviada!</h2>
        <p className="text-gray-500 leading-relaxed text-sm mb-8">
          Sua solicitação foi recebida com sucesso! A equipe NextLog analisará cuidadosamente o seu
          perfil e entrará em contato em até{" "}
          <strong className="text-brand-900">5 dias úteis</strong> pelo e-mail ou telefone informados.
          Agradecemos seu interesse em fazer parte do nosso time de motoristas.
        </p>
        <button
          onClick={onClose}
          className="px-8 py-3 bg-accent text-white font-bold rounded-xl hover:bg-accent-dark transition-all hover:-translate-y-0.5"
        >
          Fechar
        </button>
      </div>
    </div>
  );
}