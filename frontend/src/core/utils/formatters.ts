export function formatCurrency(value: number): string {
  return value.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

export function formatDate(date: string | Date): string {
  return new Date(date).toLocaleDateString("pt-BR");
}

export function formatDatetime(date: string | Date): string {
  return new Date(date).toLocaleString("pt-BR");
}

export function formatCpf(cpf: string): string {
  return cpf
    .replace(/\D/g, "")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}

export function formatCnpj(cnpj: string): string {
  return cnpj
    .replace(/\D/g, "")
    .replace(/(\d{2})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1/$2")
    .replace(/(\d{4})(\d{1,2})$/, "$1-$2");
}

export function formatDocumento(doc: string): string {
  const d = doc.replace(/\D/g, "");
  return d.length === 11 ? formatCpf(d) : formatCnpj(d);
}

export function formatPhone(phone: string): string {
  const d = phone.replace(/\D/g, "");
  if (d.length === 11)
    return d.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
  return d.replace(/(\d{2})(\d{4})(\d{4})/, "($1) $2-$3");
}

export function formatCep(cep: string): string {
  return cep.replace(/\D/g, "").replace(/(\d{5})(\d{3})/, "$1-$2");
}

export function stripMask(value: string): string {
  return value.replace(/\D/g, "");
}