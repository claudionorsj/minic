	.text
	.globl main
make:
	subq $8, %rsp
	movq %rdi, 0(%rsp)
	movq $24, %rdi
	call sbrk
	movq 0(%rsp), %rdi
	movq %rdi, 0(%rax)
	movq %rax, %rdi
	movq %rax, 16(%rdi)
	movq %rax, %rdi
	movq %rax, 8(%rdi)
	addq $8, %rsp
	ret
inserer_apres:
	subq $8, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, %rdi
	call make
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rax)
	movq 0(%rsp), %rdi
	movq %rax, 8(%rdi)
	movq %rax, %rdi
	movq 8(%rdi), %rdi
	movq %rax, 16(%rdi)
	movq 0(%rsp), %rdi
	movq %rdi, 16(%rax)
	movq $0, %rax
	addq $8, %rsp
	ret
supprimer:
	movq 8(%rdi), %rsi
	movq 16(%rdi), %rcx
	movq %rsi, 8(%rcx)
	movq 16(%rdi), %rcx
	movq 8(%rdi), %rdi
	movq %rcx, 16(%rdi)
	movq $0, %rax
	ret
afficher:
	subq $16, %rsp
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	movq %rdi, 8(%rsp)
	movq 8(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 8(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rsp)
L59:
	movq 8(%rsp), %rcx
	movq 0(%rsp), %rdi
	subq %rdi, %rcx
	cmpq $0, %rcx
	je L48
	movq 8(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 8(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rdi, 8(%rsp)
	jmp L59
L48:
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
main:
	subq $8, %rsp
	movq $65, %rdi
	call make
	movq %rax, 0(%rsp)
	movq 0(%rsp), %rdi
	call afficher
	movq 0(%rsp), %rdi
	movq $66, %rsi
	call inserer_apres
	movq 0(%rsp), %rdi
	call afficher
	movq 0(%rsp), %rdi
	movq $67, %rsi
	call inserer_apres
	movq 0(%rsp), %rdi
	call afficher
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	call supprimer
	movq 0(%rsp), %rdi
	call afficher
	movq $0, %rax
	addq $8, %rsp
	ret
	.data
