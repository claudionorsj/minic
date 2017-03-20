	.text
	.globl main
main:
	movq $16, %rdi
	call sbrk
	movq %rax, u
	movq $24, %rdi
	call sbrk
	movq %rax, s
	movq $16, %rdi
	call sbrk
	movq s, %rdi
	movq %rax, 8(%rdi)
	movq $65, %rcx
	movq s, %rdi
	movq %rcx, 0(%rdi)
	movq $66, %rcx
	movq s, %rdi
	movq %rcx, 16(%rdi)
	movq $120, %rcx
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq %rcx, 0(%rdi)
	movq $121, %rcx
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq %rcx, 8(%rdi)
	movq s, %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 16(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $88, %rcx
	movq u, %rdi
	movq %rcx, 0(%rdi)
	movq $89, %rcx
	movq u, %rdi
	movq %rcx, 8(%rdi)
	movq s, %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 16(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq u, %rcx
	movq s, %rdi
	movq %rcx, 8(%rdi)
	movq s, %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 8(%rdi), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq s, %rdi
	movq 16(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
u:
	.quad 0
s:
	.quad 0
